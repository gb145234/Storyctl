package fscut.manager.demo.util;

import fscut.manager.demo.entity.Story;
import fscut.manager.demo.service.CustomerService;
import fscut.manager.demo.vo.StoryCsvVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class CsvUtils {

    private static final String NEW_LINE_SEPARATOR = "\n";
    public static final String FILE_NAME = "writeCSV.csv";
    public static final String PATH = "D:/" + FILE_NAME;

    @Resource
    private CustomerService customerService;

    private static CsvUtils csvUtils;

    @PostConstruct
    public void init(){
        csvUtils = this;
        csvUtils.customerService = this.customerService;
    }

    private static void writeCsv(String[] headers, List<String[]> data) throws IOException {

        CSVFormat format = CSVFormat.DEFAULT.withRecordSeparator(NEW_LINE_SEPARATOR);
        FileWriter fileWriter = new FileWriter(PATH);
        CSVPrinter printer = new CSVPrinter(fileWriter,format);
        printer.printRecord(headers);
        if(data != null) {
            for(String[] lineData: data) {
                printer.printRecord(lineData);
            }
        }
        printer.flush();
        printer.close();
        log.info("CSV文件创建成功，文件路劲：" + PATH);
    }

    private static void storyToCsv(List<StoryCsvVO> data) throws IOException{
        List<String[]> result = new ArrayList<>();
        String[] headers = new String[]{"storyName","origin","putTime","storyStatus","description","conclusion","editName","designName","devName","testName","testTime","updateTime"};
        for (StoryCsvVO s:data) {
            result.add(new String[]{s.getStoryName(),
                                    s.getOrigin(),
                                    String.valueOf(s.getPutTime()),
                                    String.valueOf(s.getStoryStatus()),
                                    s.getDescription(),
                                    s.getConclusion(),
                                    s.getEditName(),
                                    s.getDesignName(),
                                    s.getDevName(),
                                    s.getTestName(),
                                    String.valueOf(s.getTestTime()),
                                    getStringDate(s.getUpdateTime())});
        }
        //CSV文件下载地址
        CsvUtils.writeCsv(headers, result);
    }

    public static void download(List<Story> stories, HttpServletResponse response) throws IOException{
        List<StoryCsvVO> result = new ArrayList<>();
        for (Story story: stories) {
            StoryCsvVO tmp = new StoryCsvVO();
            BeanUtils.copyProperties(story, tmp);
            tmp.setDesignName(csvUtils.customerService.getRealnameById(story.getDesignId()));
            tmp.setDevName(csvUtils.customerService.getRealnameById(story.getDevId()));
            tmp.setTestName(csvUtils.customerService.getRealnameById(story.getTestId()));
            tmp.setEditName(csvUtils.customerService.getRealnameById(story.getEditId()));
            result.add(tmp);
        }
        storyToCsv(result);
        readFile(response);
    }

    public static String getStringDate(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String ret = null;
        try{
            ret = sdf.format(date);
        }catch(Exception e){
            e.printStackTrace();
        }
        return ret;
    }

    private static void readFile(HttpServletResponse response){
        InputStream in = null;
        OutputStream out = null;
        try{
            in = new FileInputStream(PATH);
            int len = 0;
            byte[] buffer = new byte[1024];
            out = response.getOutputStream();
            while((len = in.read(buffer)) > 0){
                out.write(buffer,0,len);
            }
            out.flush();
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try{
                in.close();
                out.close();
            }catch(IOException e){
                e.printStackTrace();
            }

        }
    }

}
