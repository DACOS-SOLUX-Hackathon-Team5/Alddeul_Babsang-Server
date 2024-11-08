package com.example.alddeul_babsang.service;

import com.example.alddeul_babsang.entity.Menu;
import com.example.alddeul_babsang.entity.Store;
import com.example.alddeul_babsang.entity.enums.Category;
import com.example.alddeul_babsang.entity.enums.Status;
import com.example.alddeul_babsang.repository.MenuRepository;
import com.example.alddeul_babsang.repository.StoreRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.Arrays;

import static java.lang.Integer.parseInt;

@Service
@AllArgsConstructor
public class CsvImportService {

    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;

    @Transactional
    public void importDataFromCsv(String filePath) throws IOException, CsvValidationException {
        try (CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8"))) {
            String[] nextLine;
            reader.readNext(); // 첫 번째 줄은 헤더이므로 건너뜀

            while ((nextLine = reader.readNext()) != null) {
                // Store 데이터 설정
                Long id = Long.parseLong(nextLine[2]);
                String name = nextLine[3].trim();
                Category category = mapCategory(parseInt(nextLine[4].trim())); // 분류코드명 매핑
                String address = nextLine[6].trim();
                String contact = nextLine[7].trim();
                String region = nextLine[8].trim();
                double latitude = Double.parseDouble(nextLine[9].trim());
                double longitude = Double.parseDouble(nextLine[10].trim());
                String tag = nextLine[11].trim();
                Integer cluster = parseInt(nextLine[12].trim());
                Integer cluster2 = parseInt(nextLine[13].trim());
                Status status = Status.GOOD;  // 상태 기본값 설정
                LocalDateTime createdAt = LocalDateTime.now();

                Store store = new Store();
                store.setRealId(id);
                store.setName(name);
                store.setAddress(address);
                store.setRegion(region);
                store.setLatitude(latitude);
                store.setLongitude(longitude);
                store.setContact(contact);
                store.setCluster1(cluster);
                store.setCluster2(cluster2);
                store.setTop5Tags(tag);
                store.setStatus(status);
                store.setCategory(category);
                store.setCreatedAt(createdAt);

                storeRepository.save(store); // Store 데이터베이스에 저장

                // Menu 데이터 설정
                String name1 = "";
                int price1 = 0;
                String name2 = "";
                int price2 = 0;

                try {
                    if (nextLine[0] != null && !nextLine[0].isEmpty()) {
                        String[] item1 = nextLine[0].split(":");
                        name1 = item1[0].trim();
                        price1 = (item1.length > 1) ? parseInt(item1[1].replace("원", "").trim()) : 0;
                    }

                    if (nextLine[1] != null && !nextLine[1].isEmpty()) {
                        String[] item2 = nextLine[1].split(":");
                        name2 = item2[0].trim();
                        price2 = (item2.length > 1) ? parseInt(item2[1].replace("원", "").trim()) : 0;
                    }
                } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
                    e.printStackTrace();
                    System.out.println("메뉴 데이터 형식 오류 발생: " + Arrays.toString(nextLine));
                    // 기본값을 사용하여 메뉴를 계속 저장하도록 설정
                }

                Menu menu = new Menu();
                menu.setName1(name1);
                menu.setPrice1(price1);
                menu.setName2(name2);
                menu.setPrice2(price2);
                menu.setCreatedAt(createdAt);
                menu.setStore(store);  // store와 menu 관계 설정

                menuRepository.save(menu); // Menu 데이터베이스에 저장
            }
        }

    }

    private Category mapCategory(int categoryCode) {
        switch (categoryCode) {
            case 1:
                return Category.KOREAN;
            case 2:
                return Category.CHINESE;
            case 3:
                return Category.WESTERN_JAPANESE;
            default:
                return Category.OTHER;
        }
    }
}
