package fscut.manager.demo.util;

import fscut.manager.demo.entity.Story;
import fscut.manager.demo.service.CustomerService;
import fscut.manager.demo.vo.StoryCsvVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.BeanUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Slf4j
public class CsvUtils {

    private static final String NEW_LINE_SEPARATOR = "\n";
    private static final String FILE_NAME = "writeXLS.xls";
    private static final String PATH = "D:/" + FILE_NAME;

    @Resource
    private CustomerService customerService;

    private static CsvUtils csvUtils;

    @PostConstruct
    public void init() {
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
        log.info("xls文件创建成功，文件路径：" + PATH);
    }

    private static void storyToCsv(List<StoryCsvVO> data) {
        List<String[]> result = new ArrayList<>();
        String[] headers = new String[]{"storyName","origin","putTime","storyStatus","description","conclusion","editName","designName","devName","testName","testTime","updateTime"};
        for (StoryCsvVO s:data) {
            result.add(new String[]{s.getStoryName(),
                                    s.getOrigin(),
                                    String.valueOf(s.getPutTime()),
                                    String.valueOf(s.getStoryStatus()),
                                    removeHtmlTag(s.getDescription()),
                                    removeHtmlTag(s.getConclusion()),
                                    s.getEditName(),
                                    s.getDesignName(),
                                    s.getDevName(),
                                    s.getTestName(),
                                    String.valueOf(s.getTestTime()),
                                    getStringDate(s.getUpdateTime())});
        }
        //CSV文件下载地址
        try {
            CsvUtils.writeCsv(headers, result);
        } catch (IOException e) {
            log.info(e.getMessage());
        }
    }

    public static String download(List<Story> stories, HttpServletResponse response) {
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
        return PATH;
    }

    private static String getStringDate(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String ret = null;
        try{
            ret = sdf.format(date);
        }catch(Exception e){
            log.info(e.getMessage());
        }
        return ret;
    }

    private static void readFile(HttpServletResponse response) {
        try(InputStream in = new FileInputStream(PATH);
            OutputStream out = response.getOutputStream()
        )
        {
            int len;
            byte[] buffer = new byte[1024];
            while((len = in.read(buffer)) > 0) {
                out.write(buffer,0, len);
            }
            out.flush();
        }catch(IOException e) {
            log.info(e.getMessage());
        }
    }


    /**
     * 去除富文本编辑器标签
     * @param inputString 富文本字符串
     * @return 纯文本
     */
    private static String removeHtmlTag(String inputString) {
        if (inputString == null) {
            return null;
        }

        // 含html标签的字符串
        String htmlStr = inputString;
        String textStr = "";
        Pattern pScript;
        Pattern pStyle;
        Pattern pHtml;
        Matcher mScript;
        Matcher mStyle;
        Matcher mHtml;
        try {
            //定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>
            String regExScript = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";
            //定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>
            String regExStyle = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";
            // 定义HTML标签的正则表达式
            String regExHtml = "<[^>]+>";
            pScript = Pattern.compile(regExScript, Pattern.CASE_INSENSITIVE);
            mScript = pScript.matcher(htmlStr);
            // 过滤script标签
            htmlStr = mScript.replaceAll("");
            pStyle = Pattern.compile(regExStyle, Pattern.CASE_INSENSITIVE);
            mStyle = pStyle.matcher(htmlStr);
            // 过滤style标签
            htmlStr = mStyle.replaceAll("");
            pHtml = Pattern.compile(regExHtml, Pattern.CASE_INSENSITIVE);
            mHtml = pHtml.matcher(htmlStr);
            // 过滤html标签
            htmlStr = mHtml.replaceAll("");
            textStr = htmlStr;
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        // 返回文本字符串
        return textStr;
    }

    public static ResponseEntity<FileSystemResource> export(File file) {
        if (file == null) {
            return null;
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Content-Disposition", "attachment; filename=" + "writeXLS.xls");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        headers.add("Last-Modified", new Date().toString());
        headers.add("ETag", String.valueOf(System.currentTimeMillis()));

        return ResponseEntity.ok().headers(headers).contentLength(file.length()).contentType(MediaType.parseMediaType("application/octet-stream")).body(new FileSystemResource(file));
    }

}
