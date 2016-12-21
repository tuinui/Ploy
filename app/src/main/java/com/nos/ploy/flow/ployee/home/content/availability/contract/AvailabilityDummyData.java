//package com.nos.ploy.flow.ployee.home.content.availability.contract;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by Saran on 22/11/2559.
// */
//
//public class AvailabilityDummyData {
//
//    public static List<AvailabilityViewModel> DAYS_HEADER_DATA = new ArrayList<>();
//    public static List<NormalItemAvailabilityVM> AVAILABILITY_IN_7_DAYS = new ArrayList<>();
//
//    static {
//        DAYS_HEADER_DATA.add(new HeaderDayAvailabilityVM(null));
//        DAYS_HEADER_DATA.add(new HeaderDayAvailabilityVM("S"));
//        DAYS_HEADER_DATA.add(new HeaderDayAvailabilityVM("M"));
//        DAYS_HEADER_DATA.add(new HeaderDayAvailabilityVM("T"));
//        DAYS_HEADER_DATA.add(new HeaderDayAvailabilityVM("W"));
//        DAYS_HEADER_DATA.add(new HeaderDayAvailabilityVM("Th"));
//        DAYS_HEADER_DATA.add(new HeaderDayAvailabilityVM("F"));
//        DAYS_HEADER_DATA.add(new HeaderDayAvailabilityVM("S"));
//
//        for (int i = 0; i < 7; i++) {
//            AVAILABILITY_IN_7_DAYS.add(new NormalItemAvailabilityVM(i % 3 == 0));
//        }
//    }
//
//    public static List<AvailabilityViewModel> getDummyData() {
//        List<String> timeRanges = new ArrayList<>();
//        timeRanges.add("09:00 - 10:00");
//        timeRanges.add("10:00 - 11:00");
//        timeRanges.add("11:00 - 12:00");
//        timeRanges.add("12:00 - 15:00");
//        timeRanges.add("15:00 - 18:00");
//        timeRanges.add("18:00 - 19:00");
//        timeRanges.add("19:00 - 21:00");
//
//        List<AvailabilityViewModel> datas = new ArrayList<>();
//        datas.addAll(DAYS_HEADER_DATA);
//        for (String timeRange : timeRanges) {
//            datas.add(new HeaderTimeAvailabilityVM(timeRange));
//            datas.addAll(AVAILABILITY_IN_7_DAYS);
//        }
//        return datas;
//    }
//
//
//
//}
