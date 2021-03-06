package furamaResort.services;

import furamaResort.libs.CheckProperty;
import furamaResort.libs.QuickInOut;
import furamaResort.models.*;

import furamaResort.utils.ReadAndWriteMap;
import furamaResort.utils.ValidateInputData;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

public class FacilityServiceImpl implements FacilityService {
    private static Map<Facility, Integer> facilityMap = new LinkedHashMap<>();
    private static Map<Villa, Integer> villaMap = new LinkedHashMap<>(); //nếu data-type là Facility --> khi edit, không thể gọi thuộc tính riêng của từng service
    private static Map<House, Integer> houseMap = new LinkedHashMap<>();
    private static Map<Room, Integer> roomMap = new LinkedHashMap<>();

    //villa
    private static ReadAndWriteMap<Villa, Integer> villaFile = new ReadAndWriteMap<>();
    private static final String PATH_VILLA = "D:\\module2\\src\\furamaResort\\data\\villa";
    //house
    private static ReadAndWriteMap<House, Integer> houseFile = new ReadAndWriteMap<>();
    private static final String PATH_HOUSE = "D:\\module2\\src\\furamaResort\\data\\house";
    //room
    private static ReadAndWriteMap<Room, Integer> roomFile = new ReadAndWriteMap<>();
    private static final String PATH_ROOM = "D:\\module2\\src\\furamaResort\\data\\room";

    //khoi tạo giá trị cho list từ file:
    static {
        villaMap = villaFile.readData(PATH_VILLA);
        houseMap = houseFile.readData(PATH_HOUSE);
        roomMap = roomFile.readData(PATH_ROOM);
        facilityMap.putAll(villaMap);
        facilityMap.putAll(houseMap);
        facilityMap.putAll(roomMap);
    }

//    public static void main(String[] args) {////NG: chưa wite value mới vào file house/room/villa..
//
//    }


    @Override
    public void display() {
        if (villaMap.size() == 0 && houseMap.size() == 0 && roomMap.size() == 0) {
            System.err.println("Facility list is EMPTY");
        } else {
            for (Facility service : facilityMap.keySet()) {
                System.out.println(service);
            }
        }
    }

    @Override
    public void displayMaintainList() {
        Collection<Booking> bookings = BookingServiceIplm.getBookingSet();
        for (Booking booking : bookings) { //VD: 5 booking - 10 facility
            for (Map.Entry<Facility, Integer> service : facilityMap.entrySet()) {
                if (booking.getServiceName().equals(service.getKey().getServiceCode()) && service.getValue() < 5) {
                    int nums = service.getValue() + 1;
                    facilityMap.put(service.getKey(), nums);
                    //ghi nhận giá trị mới vào file
                    if (service.getKey() instanceof Villa) {
                        villaMap.put((Villa) service.getKey(), nums);
                        villaFile.writeData(PATH_VILLA, villaMap);
                    } else if (service.getKey() instanceof House) {
                        houseMap.put((House) service.getKey(), nums);
                        houseFile.writeData(PATH_HOUSE, houseMap);
                    } else {
                        roomMap.put((Room) service.getKey(), nums);
                        roomFile.writeData(PATH_ROOM, roomMap);
                    }
                }
            }
        }
        //in list cần bảo trì:
        int count = 0;
        for (Map.Entry<Facility, Integer> service : facilityMap.entrySet()) {
            if (service.getValue() == 5) {
                System.out.println(service);
                count++;
            }
        }
        if (count == 0) {
            System.err.println("No facility need to maintain ");
        }
    }

    public void addNewVilla() {  //ok
        //nhap villaCode -  check code trùng?
        String villaCode = null;
        boolean check = false;
        while (!check) {
            int count = 0;
            villaCode = ValidateInputData.villaCode();  //regex khác nhau --> không tạo method check chung cho 3 loại đc!
            for (Villa villa : villaMap.keySet()) {
                if (!villaCode.equals(villa.getServiceCode())) {
                    count++;
                }
            }
            if (count == villaMap.size()) {
                System.out.println("ok");
                check = true;
            } else {
                System.err.println("Villa Code cannot be duplicated! Please enter again: ");
            }
        }
        double useArea = ValidateInputData.area();
        double rentalFee = ValidateInputData.rentalFee();  //Note: sử dụng Ex của float cho double -->kq check runtime : chỉ hiển thị được tới range của Float, vượt --> báo sai regex
        int maxNumsPeople = ValidateInputData.maxNumsPeople();
        //nhập kiểu thuê:
        System.out.println("Enter rental type: "); //không cần regex, vì chỉ có 4 lựa chọn cố định
        String[] villaRentalTypes = {"VL-year", "VL-month", "VL-day", "VL-hour"};
        String rentalType = CheckProperty.checkInputProperty(villaRentalTypes);
        //nhập standard:
        String villaStandard = ValidateInputData.villaStandard();
        //pool
        System.out.println("Enter pool : ");
        double poolArea = ValidateInputData.area();
        //floors
        int floors = ValidateInputData.numsOfFloors();
        villaMap.put(new Villa(villaCode, useArea, rentalFee, maxNumsPeople, rentalType, villaStandard, poolArea, floors), 0);
        villaFile.writeData(PATH_VILLA, villaMap);
        facilityMap.putAll(villaMap);
    }

    public void addNewHouse() {
        //nhap houseCode -  check code trùng?
        String houseCode = null;
        boolean check = false;
        while (!check) {
            int count = 0;
            houseCode = ValidateInputData.houseCode();
            for (House house : houseMap.keySet()) {
                if (!houseCode.equals(house.getServiceCode())) {
                    count++;
                }
            }
            if (count == houseMap.size()) {
                System.out.println("ok");
                check = true;
            } else {
                System.err.println("House Code cannot be duplicated! Please enter again: ");
            }
        }

        double useArea = ValidateInputData.area();
        double rentalFee = ValidateInputData.rentalFee();
        int maxNumsPeople = ValidateInputData.maxNumsPeople();
        //nhập kiểu thuê:
        System.out.println("Enter rental type: ");
        String[] villaRentalTypes = {"HO-year", "HO-month", "HO-day", "HO-hour"};
        String rentalType = CheckProperty.checkInputProperty(villaRentalTypes);
        //nhập standard:
        String houseStandard = ValidateInputData.houseStandard();
        //floors
        int floors = ValidateInputData.numsOfFloors();
        houseMap.put(new House(houseCode, useArea, rentalFee, maxNumsPeople, rentalType, houseStandard, floors), 0);
        houseFile.writeData(PATH_HOUSE, houseMap);
        facilityMap.putAll(houseMap);
    }

    public void addNewRoom() {
        //nhap roomCode -  check code trùng?
        String roomCode = null;
        boolean check = false;
        while (!check) {
            int count = 0;
            roomCode = ValidateInputData.roomCode();
            for (Room room : roomMap.keySet()) {
                if (!roomCode.equals(room.getServiceCode())) {
                    count++;
                }
            }
            if (count == roomMap.size()) {
                System.out.println("ok");
                check = true;
            } else {
                System.err.println("Room Code cannot be duplicated! Please enter again: ");
            }
        }
        //
        double useArea = ValidateInputData.area();
        double rentalFee = ValidateInputData.rentalFee();
        int maxNumsPeople = ValidateInputData.maxNumsPeople();
        //nhập kiểu thuê:
        System.out.println("Enter rental type: ");
        String[] villaRentalTypes = {"RO-year", "RO-month", "RO-day", "RO-hour"};
        String rentalType = CheckProperty.checkInputProperty(villaRentalTypes);
        //tên dịch vụ đi kèm (Accompanied Service):
        String accompService = QuickInOut.inputOutput("Enter Accompanied Service: ");
        roomMap.put(new Room(roomCode, useArea, rentalFee, maxNumsPeople, rentalType, accompService), 0);
        roomFile.writeData(PATH_ROOM, roomMap);
        facilityMap.putAll(roomMap);
    }

    @Override
    public void editVila() {//ok
        boolean check = false;
        while (!check) {
            String villaCode = ValidateInputData.villaCode();
            for (Villa service : villaMap.keySet()) {
                if (villaCode.equals(service.getServiceCode())) {
                    check = true;
                    service.setUseArea(ValidateInputData.area());
                    service.setRentalFees(ValidateInputData.rentalFee());
                    service.setMaxNumsPeople(ValidateInputData.maxNumsPeople());
                    System.out.println("Enter rental type: "); //không cần regex, vì chỉ có 4 lựa chọn cố định
                    String[] villaRentalTypes = {"VL-year", "VL-month", "VL-day", "VL-hour"};
                    service.setRentalType(CheckProperty.checkInputProperty(villaRentalTypes));
                    service.setStandard(ValidateInputData.villaStandard());
                    service.setPoolArea(ValidateInputData.area());
                    service.setFloors(ValidateInputData.numsOfFloors());
                    villaFile.writeData(PATH_VILLA, villaMap);
                    facilityMap.putAll(villaFile.readData(PATH_VILLA));
                    break;
                }
            }
            if (!check) {
                System.out.println("NOT found this villa code. Please try again: ");
            }
        }
    }

    @Override
    public void editHouse() {
        boolean check = false;
        while (!check) {
            String houseCode = ValidateInputData.houseCode();
            for (House service : houseMap.keySet()) {
                if (houseCode.equals(service.getServiceCode())) {
                    check = true;
                    service.setUseArea(ValidateInputData.area());
                    service.setRentalFees(ValidateInputData.rentalFee());
                    service.setMaxNumsPeople(ValidateInputData.maxNumsPeople());
                    System.out.println("Enter rental type: "); //không cần regex, vì chỉ có 4 lựa chọn cố định
                    String[] villaRentalTypes = {"VL-year", "VL-month", "VL-day", "VL-hour"};
                    service.setRentalType(CheckProperty.checkInputProperty(villaRentalTypes));
                    service.setStandard(ValidateInputData.villaStandard());
                    service.setFloors(ValidateInputData.numsOfFloors());
                    houseFile.writeData(PATH_HOUSE, houseMap);
                    facilityMap.putAll(houseFile.readData(PATH_HOUSE));
                    break;
                }
            }
            if (!check) {
                System.out.println("NOT found this villa code. Please try again: ");
            }
        }
    }

    @Override
    public void editRoom() { //NG:
//        Map<Room,Integer> servicesMap = roomFile.readData(PATH_ROOM);  //tạo list hứng kết quả --> ghi đè trở lại file
        boolean check = false;
        while (!check) {
            String roomCode = ValidateInputData.roomCode();
            for (Room service : roomMap.keySet()) {
                if (roomCode.equals(service.getServiceCode())) {
                    check = true;
                    service.setUseArea(ValidateInputData.area());
                    service.setRentalFees(ValidateInputData.rentalFee());
                    service.setMaxNumsPeople(ValidateInputData.maxNumsPeople());
                    System.out.println("Enter rental type: "); //không cần regex, vì chỉ có 4 lựa chọn cố định
                    String[] villaRentalTypes = {"VL-year", "VL-month", "VL-day", "VL-hour"};
                    service.setRentalType(CheckProperty.checkInputProperty(villaRentalTypes));
                    service.setFreeServices(QuickInOut.inputOutput("Enter free services name: "));
                    roomFile.writeData(PATH_ROOM, roomMap);
                    facilityMap.putAll(roomFile.readData(PATH_ROOM));
                    break;
                }
            }
            if (!check) {
                System.out.println("NOT found this villa code. Please try again: ");
            }
        }
    }

    public static Map<Facility, Integer> getFacilityMap() {
        return facilityMap;
    }
}
/*note:
1. NG - Chức năng Edit(): Tại sao
+ Nếu đọc file --> gán kết quả cho list mới khởi tạo --> Vẫn edit thành công, nhưng nếu đang trong luồng chạy --> sau khi edit--> display --> không update thông tin mới.
 --> Nhưng nếu thoát luồng, chạy lại -->ok
+ Nếu gọi sử dụng trực tiếp "roomMap" --> OK
2. NG - display(): mỗi loại dịch vụ chỉ display ra đc những thuộc tính riêng --> writeFile() đang có vấn đề?? hay readFile()??/
--> do chưa toString()
2.  làm sao để hiển thị số tiền theo định dạng $, nhưng vẫn là kiểu số thực?
   vì nếu để là double --> nếu số tiền >=100tr --> vượt range
   2. hàm edit() phải viết 3 method riêng??
   3. file data: có thể lưu hết 3 map 3 loại service vào cùng 1 file "facility" để tiện thao tác k?

   4. NOTE Kinh nghiệm: Ex InvalidClassException....: xảy ra khi: file data rỗng --> lỗi
    khắc phục: cho khối static khởi tạo giá trị cho vilaMap/houseMap..--> writeFile --> display lại -->ok
*/
