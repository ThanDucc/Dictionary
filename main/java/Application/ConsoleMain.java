package Application;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class ConsoleMain {

    public static void main(String[] args) throws InterruptedException {

        DictionaryCommandline dictionaryCommandline = new DictionaryCommandline();

        System.out.println("Chào mừng bạn đến với từ điển: ");
        int selection;
        do {
            TimeUnit.SECONDS.sleep(1);
            System.out.println();
            System.out.println("|--------------------------- TỪ ĐIỂN ---------------------------|");
            System.out.println("|---------------------------------------------------------------|");
            System.out.println("|---------- 1: NHẬP DỮ LIỆU VÀO TỪ ĐIỂN BẰNG BÀN PHÍM ----------|");
            System.out.println("|---------------------------------------------------------------|");
            System.out.println("|---------- 2: NHẬP DỮ LIỆU VÀO TỪ ĐIỂN TỪ FILE ----------------|");
            System.out.println("|---------------------------------------------------------------|");
            System.out.println("|---------- 3: THÊM TỪ MỚI VÀO TỪ ĐIỂN -------------------------|");
            System.out.println("|---------------------------------------------------------------|");
            System.out.println("|---------- 4: XOÁ TỪ TRONG TỪ ĐIỂN ----------------------------|");
            System.out.println("|---------------------------------------------------------------|");
            System.out.println("|---------- 5: SỬA TỪ TRONG TỪ ĐIỂN ----------------------------|");
            System.out.println("|---------------------------------------------------------------|");
            System.out.println("|---------- 6: TÌM KIẾM TỪ TRONG TỪ ĐIỂN -----------------------|");
            System.out.println("|---------------------------------------------------------------|");
            System.out.println("|---------- 7: TÌM KIẾM TỪ CÁC TỪ GỢI Ý TRONG TỪ ĐIỂN ----------|");
            System.out.println("|---------------------------------------------------------------|");
            System.out.println("|---------- 8: HIỂN THỊ TỪ ĐIỂN --------------------------------|");
            System.out.println("|---------------------------------------------------------------|");
            System.out.println("|---------- 9: XUẤT DỮ LIỆU TỪ ĐIỂN RA FILE --------------------|");
            System.out.println("|---------------------------------------------------------------|");
            System.out.println("|---------- 10: THOÁT CHƯƠNG TRÌNH -----------------------------|");
            System.out.println("|---------------------------------------------------------------|");
            System.out.println();
            System.out.println("--> Mời bạn nhập lựa chọn: ");
            Scanner sc = new Scanner(System.in);
            selection = sc.nextInt();
            switch (selection) {
                case 1: {
                    dictionaryCommandline.dictionaryManagement.insertFromCommandline();
                    break;
                }
                case 2: {
                    dictionaryCommandline.dictionaryManagement.insertFromFile();
                    break;
                }
                case 3: {
                    dictionaryCommandline.dictionaryManagement.addDictionary();
                    break;
                }
                case 4: {
                    dictionaryCommandline.dictionaryManagement.removeDictionary();
                    break;
                }
                case 5: {
                    dictionaryCommandline.dictionaryManagement.editDictionary();
                    break;
                }
                case 6: {
                    dictionaryCommandline.dictionaryManagement.dictionaryLookup();
                    break;
                }
                case 7: {
                    dictionaryCommandline.dictionarySeacher();
                    break;
                }
                case 8: {
                    dictionaryCommandline.showAllWords();
                    break;
                }
                case 9: {
                    dictionaryCommandline.dictionaryManagement.dictionaryExportToFile();
                    break;
                }
                case 10: {
                    System.out.println("----- Exit program -----");
                    System.exit(0);
                }
                default: {
                    System.out.println("\n[Bạn đã nhập sai lựa chọn]");
                }

            }
        } while (true);

    }

}
