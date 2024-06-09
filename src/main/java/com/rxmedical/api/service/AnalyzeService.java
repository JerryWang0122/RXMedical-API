package com.rxmedical.api.service;

import com.rxmedical.api.model.po.History;
import com.rxmedical.api.model.po.Record;
import com.rxmedical.api.repository.HistoryRepository;
import com.rxmedical.api.repository.RecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AnalyzeService {

    @Autowired
    private RecordRepository recordRepository;
    @Autowired
    private HistoryRepository historyRepository;

    public Map<String, Integer> getLaborScore() {
        // 分數加成，可自行調整
        final int TRANSPORTER_FACTOR = 2;
        final int TAKER_FACTOR = 5;

        // 找出近30日，完成的申請單
        // 獲取當前日期和時間
        Calendar calendar = Calendar.getInstance();

        // 將日期設置為30天前
        calendar.add(Calendar.DAY_OF_YEAR, -30);

        // 獲取計算後的日期
        Date monthAgoDate = calendar.getTime();
        List<Record> finishRecords = recordRepository.findByStatusAndUpdateDateAfter("finish", monthAgoDate);

        Map<String, Integer> map = new HashMap<>();
        finishRecords.forEach(record -> {
            List<History> recordDetails = historyRepository.findByRecord(record);

            // 计算撿貨人分數
            recordDetails.stream()
                    .map(history -> history.getUser().getName())
                    .forEach(taker -> map.merge(taker, TAKER_FACTOR, Integer::sum));

            // 计算運送人分數
            String transporterName = record.getTransporter().getName();
            // 運送分 = 訂單明細數 * 運送加成
            int transportScore = recordDetails.size() * TRANSPORTER_FACTOR;
            map.merge(transporterName, transportScore, Integer::sum);
        });
        return map;
    }
}
