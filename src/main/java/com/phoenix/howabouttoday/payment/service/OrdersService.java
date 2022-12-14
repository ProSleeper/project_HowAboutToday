package com.phoenix.howabouttoday.payment.service;

import com.phoenix.howabouttoday.global.OrdersStatus;
import com.phoenix.howabouttoday.member.dto.MemberDTO;
import com.phoenix.howabouttoday.member.entity.Member;
import com.phoenix.howabouttoday.member.repository.MemberRepository;
import com.phoenix.howabouttoday.payment.dto.OrdersDeleteDTO;
import com.phoenix.howabouttoday.payment.dto.OrdersDetailVO;
import com.phoenix.howabouttoday.payment.dto.OrdersCreateDTO;
import com.phoenix.howabouttoday.payment.dto.OrdersDirectDTO;
import com.phoenix.howabouttoday.payment.entity.Coupon;
import com.phoenix.howabouttoday.payment.entity.Orders;
import com.phoenix.howabouttoday.payment.entity.OrdersDetail;
import com.phoenix.howabouttoday.payment.enumType.ReviewStatus;
import com.phoenix.howabouttoday.payment.repository.AvailableDateRepository;
import com.phoenix.howabouttoday.payment.repository.CouponRepository;
import com.phoenix.howabouttoday.payment.repository.OrdersRepository;
import com.phoenix.howabouttoday.reserve.domain.CartRepository;
import com.phoenix.howabouttoday.reserve.domain.Reservation.Cart;
import com.phoenix.howabouttoday.reserve.domain.Reservation.ReserveStatus;
import com.phoenix.howabouttoday.room.entity.AvailableDate;
import com.phoenix.howabouttoday.room.entity.Room;
import com.phoenix.howabouttoday.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class OrdersService {

    private final CartRepository cartRepository;
    private final AvailableDateRepository availableDateRepository;
    private final MemberRepository memberRepository;
    private final OrdersRepository ordersRepository;
    private final RoomRepository roomRepository;
    private final CouponRepository couponRepository;



    public Boolean cartDuplCheck(MemberDTO memberDTO, Long roomNum){
        /** ??????????????? ????????? ????????? ???????????????. **/
        if (cartRepository.existsByMember_MemberNumAndRoom_RoomNum(memberDTO.getMemberNum(), roomNum)){
            return true;
        }
        return false;
    }


    /** ?????? -> ??????????????? ???????????? ????????? ?????? ????????? ???????????? ????????????. **/
    public List<OrdersDetailVO> getDirectData(MemberDTO memberDTO, OrdersDirectDTO ordersDirectDTO){

        Member member = memberRepository.findById(memberDTO.getMemberNum()).orElseThrow(() -> new IllegalArgumentException(memberDTO.getMemberNum() + "??? ?????? ????????? ????????????."));
        Room storeRoom = roomRepository.findById(ordersDirectDTO.getRoomNum()).orElseThrow(() -> new IllegalArgumentException(ordersDirectDTO.getRoomNum() + "??? Room ????????? ????????????."));

        String[] splitDate = ordersDirectDTO.getDaterange().split("-");
        LocalDate startDate = StringToParseDate(splitDate[0].strip());
        LocalDate endDate = StringToParseDate(splitDate[1].strip());

        Period between = Period.between(startDate, endDate);

        Cart saveCart = cartRepository.save(Cart.builder()
                .member(member)
                .room(storeRoom)
                .reserveStatus(ReserveStatus.READY)
                .reserveUseStartDate(startDate)
                .reserveUseEndDate(endDate)
                .reservePrice(storeRoom.getPrice() * between.getDays())
                .reserveAdultCount(ordersDirectDTO.getAdult_number())
                .reserveChildCount(ordersDirectDTO.getChild_number())
                .build());

        OrdersDetailVO ordersDetailVO = new OrdersDetailVO(saveCart);

        List<OrdersDetailVO> ordersDetailVOList = new ArrayList<>();
        ordersDetailVOList.add(ordersDetailVO);

        return ordersDetailVOList;
    }


    /** ?????????????????? LocalDate???????????? ??????????????? ????????? **/
    public LocalDate StringToParseDate(String date){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate parseDate = LocalDate.parse(date, formatter);
        return parseDate;
    }


    /** ?????????????????? ???????????? ??????????????? ?????????. **/
    public List<OrdersDetailVO> getCartData(List<Long> cartNum){
        return cartRepository.findAllById(cartNum)
                .stream()
                .map(OrdersDetailVO::new)
                .collect(Collectors.toList());
    }

    /** ????????? ???????????? ?????? ???????????? ?????? **/
    public boolean savePaymentData(Long memberNum, OrdersCreateDTO ordersCreateDTO){
        try {
            Member member = memberRepository.findById(memberNum).orElseThrow(() -> new IllegalArgumentException(String.format("%d??? ????????? ????????????.", memberNum)));
            List<Cart> cartList = cartRepository.findAllById(ordersCreateDTO.getCartNum());


            /** ??????????????? ???????????? ???. **/
            //?????? ?????? ??? ?????? ?????? ????????? ??????pk??? ????????? ??????. ?????????(?????? ????????? ??????)??? orders???????????? ????????? ?????? ????????????.

            Orders order = getOrder(ordersCreateDTO, member, cartList);
            List<OrdersDetail> lists = cartList // Entity List
                    .stream() // Entity Stream
                    .map(cart -> ordersNumberMapping(cart, order)) // DTO Stream
                    .collect(Collectors.toList()); // DTO List

            order.getReservation().addAll(lists);

            Coupon coupon = couponRepository.findByCouponNumAndMember_MemberNum(ordersCreateDTO.getUseCouponNum(), memberNum).orElse(null);

            if(coupon != null){
                coupon.couponUsed();
            }

            ordersRepository.save(order);
            cartRepository.deleteAllById(ordersCreateDTO.getCartNum());
        }
        catch (RuntimeException e){
            System.out.println(e.toString());
            return false;
        }
        return true;
    }

    /** ?????? ????????? ????????? ??????????????? ???????????? ?????????. **/
    /** ?????? ?????? orders ????????? ???????????? ?????? ?????? ?????????... **/
    private Orders getOrder(OrdersCreateDTO ordersCreateDTO, Member member, List<Cart> cartList) {
        Boolean isToday = false;
        for (Cart cart :cartList) {
            if(cart.getReserveUseStartDate().isEqual(LocalDate.now())){
                isToday = true;
            }
        }

        Orders order = Orders.builder()
                .member(member)
                .ordersName(ordersCreateDTO.getName())
                .ordersTel(ordersCreateDTO.getTel())
                .ordersDate(LocalDateTime.now())
                .ordersPrice(getTotalPrice(cartList
                        .stream()
                        .map(Cart::getReserveNum)
                        .collect(Collectors.toList())))
                .ordersType(ordersCreateDTO.getOrdersType())
                .ordersStatus(isToday ? OrdersStatus.IN_USE : OrdersStatus.PAYMENT_COMPLETE)
                .merchantId(ordersCreateDTO.getMerchantId())
                .impUid(ordersCreateDTO.getImp_uid())
                .couponNum(ordersCreateDTO.getUseCouponNum())
                .discountValue(ordersCreateDTO.getDiscountValue())
                .build();
        return order;
    }

    /** ????????????????????? ????????? ??? ????????? ??????. **/
    public Integer getTotalPrice(List<Long> cartNum){
        List<OrdersDetailVO> ordersDetailVOList = cartRepository.findAllById(cartNum).stream()
                .map(OrdersDetailVO::new)
                .collect(Collectors.toList());

        return ordersDetailVOList.stream()
                .mapToInt(OrdersDetailVO::totalPrice).sum();
    }

    /** ?????? ????????? cart??? ?????? ????????? orderDetail??? ???????????? ?????? **/
    private OrdersDetail ordersNumberMapping(Cart cart, Orders order){

        //?????? ????????? ???????????? ????????????.
        LocalDate startDate = cart.getReserveUseStartDate();
        LocalDate endDate =  cart.getReserveUseEndDate();

        Period period = Period.between(startDate, endDate);
        System.out.println("????????????: " + period.getMonths() * 30 + period.getDays());

        OrdersDetail od = OrdersDetail.builder()
                .member(order.getMember())
                .room(cart.getRoom())
                .orders(order)
                .reserveStatus(cart.getReserveUseStartDate().isEqual(LocalDate.now()) ? ReserveStatus.IN_USE : ReserveStatus.READY)
                .reserveUseStartDate(cart.getReserveUseStartDate())
                .reserveUseEndDate(cart.getReserveUseEndDate())
                .reservePrice(cart.getReservePrice())
                .reserveAdultCount(cart.getReserveAdultCount())
                .reserveChildCount(cart.getReserveChildCount())
                .isReviewWrited(ReviewStatus.PRE_WRITE)
                .build();


        for (int i = 0; i < period.getMonths() * 30 + period.getDays(); i++) {
            AvailableDate ad = AvailableDate.builder()
                    .date(startDate.plusDays(i))
                    .room(od.getRoom())
                    .build();
            od.getRoom().getAvailableDate().add(ad);
        }
        return od;
    }

    public void changeStatusOrders(Long ordersNum){
        if (ordersNum != -1){

            Orders orders = ordersRepository.findById(ordersNum).orElseThrow(()->new IllegalArgumentException("?????? Orders??? ????????????."));
            orders.getReservation()
                    .forEach(orderDetail -> {
                        LocalDate startDate = orderDetail.getReserveUseStartDate();
                        LocalDate endDate = orderDetail.getReserveUseEndDate().minusDays(1);
                        availableDateRepository.deleteAllByRoom_RoomNumAndOneDayBetween(orderDetail.getRoom().getRoomNum(), startDate, endDate);
                        orderDetail.changeToCancel();
                    });
            orders.changeStatusToCancel();
        }
    }
    public Long cancelOrders(OrdersDeleteDTO ordersDeleteDTO){

        Orders orders = ordersRepository.findByMerchantId(ordersDeleteDTO.getMerchant_uid()).orElseThrow(() -> new IllegalArgumentException("uid??? ????????????."));

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("reason", ordersDeleteDTO.getReason());
        body.add("imp_uid", orders.getImpUid());
        body.add("amount", ordersDeleteDTO.getCancel_request_amount().toString());
        body.add("checksum", ordersDeleteDTO.getCancel_request_amount().toString());

        /** ??????????????? ?????? ?????? ??????. ????????? ???????????? ????????? ??????????????? ?????? ?????? **/
//        body.add("refund_holder", "?????????");
//        body.add("refund_bank", "88");
//        body.add("refund_account", "110218317400");

        Mono<String> result = WebClient.create() //????????????
                .post() //????????????
                .uri("https://api.iamport.kr/payments/cancel")   //?????? uri
                .contentType(MediaType.APPLICATION_JSON)  //????????? ???????????? ??????
                .bodyValue(body)    //body??? ?????? ?????????. ?????? ????????? ?????? ????????? ??????.
                .accept(MediaType.APPLICATION_JSON) //?????? ???????????? ??????
                .header("Authorization", getToken())
                .retrieve() //?????? ????????? ????????? ????????? ???????????????
                .bodyToMono(String.class);  //????????? ????????? ????????? ??????????????? ????????????


        // System.out.println(result.block());
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = null;

        try {
            jsonObject = (JSONObject) jsonParser.parse(result.block());
        }
        catch (ParseException e){
            System.out.println(e.toString());
        }
        System.out.println(jsonObject.get("code"));

        return ((Long)jsonObject.get("code") == 0) ? orders.getOrdersNum() : -1;
    }

    public String getToken(){
        String IMP_KEY = "3220511523750621";
        String IMP_SECRET = "Wlbixtnz3pYJ6wegOQ7FCJ0RRC3DGwDYsspAVKWWPv8z3mFEi5mlI663orvNeIm15VaypSopH8ujfoe7";

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("imp_key", IMP_KEY);
        body.add("imp_secret", IMP_SECRET);

        Mono<String> result = WebClient.create() //????????????
                .post() //????????????
                .uri("https://api.iamport.kr/users/getToken")   //?????? uri
//                .contentType(MediaType.APPLICATION_JSON)  //????????? ???????????? ??????
                .bodyValue(body)    //body??? ?????? ?????????. ?????? ????????? ?????? ????????? ??????.
                .accept(MediaType.APPLICATION_JSON) //?????? ???????????? ??????
                .retrieve() //?????? ????????? ????????? ????????? ???????????????
                .bodyToMono(String.class);  //????????? ????????? ????????? ??????????????? ????????????


        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = null;
        try {
            jsonObject = (JSONObject) jsonParser.parse(result.block());
        }
        catch (ParseException e){
            System.out.println(e.toString());
        }
        JSONObject response = ((JSONObject)jsonObject.get("response"));
        return (String)response.get("access_token");
    }
}
