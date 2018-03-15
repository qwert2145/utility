package com.my;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2018/3/15.
 */
//结清
//SELECT a.lBorrowerId,a.strName,a.strIdentity,a.strMobile,a.lBorrowIntentId,a.strLoanAcctNo,DATE_FORMAT(b.dtLoanTime,'%Y-%m-%d') dtLoanTime,DATE_FORMAT(a.strBorrowEndDate,'%Y-%m-%d') strBorrowEndDate,DATE_FORMAT(a.strStartDate,'%Y-%m-%d') strStartDate,DATE_FORMAT(a.strRealRepayDate,'%Y-%m-%d') strRealRepayDate,a.lPrincipal from tbTaskRepay a LEFT JOIN tbBorrow b
//        on a.lBorrowIntentId = b.lBorrowIntentId where nDeductState = 2 and nNoticeState = 2 and nApplyState =2 and lPrincipal > 0 and a.dtCreateTime < '2018-03-15 00:00:00';

//未结清
//        SELECT lBorrowerId,strName,strIdentity,strMobile,lBorrowIntentId,strLoanAcctNo,DATE_FORMAT(dtLoanTime,'%Y-%m-%d') dtLoanTime,DATE_FORMAT(DATE_ADD(dtLoanTime,INTERVAL 89 DAY),'%Y-%m-%d') strBorrowEndDate,lAmount from tbBorrow where nState = 1 and
//        lBorrowIntentId not in (SELECT lBorrowIntentId from tbTaskRepay where nDeductState = 2 and nNoticeState = 2 and nApplyState =2 and lPrincipal > 0 and dtCreateTime < '2018-03-15 00:00:00');

public class ExcelExport {
    public static void main(String[] args) throws Exception {
        String sheetName = "未结清数据";
        int columnNumber = 9;
        String[] columnName = { "用户id", "姓名", "身份证","手机","意向编号","中银消费账号","放款成功日","借款到期日","借款本金" };
//        String sheetName = "结清数据";
//        int columnNumber = 11;
//        String[] columnName = { "用户id", "姓名", "身份证","手机","意向编号","中银消费账号","放款成功日","借款到期日","最后一次还款发起日","最后一次还款成功日","借款本金" };
        new ExcelExport().ExportNoResponse(sheetName,
                columnNumber, columnName);
    }

    public void ExportNoResponse(String sheetName,int columnNumber,
                                 String[] columnName) throws Exception {
        //创建一个webbook，对应一个Excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        //在webbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet(sheetName);
        //创建第1行 也就是表头
        HSSFRow row = sheet.createRow((int) 0);

        //创建表头的列
        for (int i = 0; i < columnNumber; i++)
        {
            HSSFCell cell = row.createCell(i);
            cell.setCellValue(columnName[i]);

        }
        String file = "D:\\ziptest\\zy_未结清.xlsx";
        List<Map<String, String>> list = ExcelReader.readExcelContent(file);
        Map<String, String> map = null;
        AesUtil aesUtil = new AesUtil();
        aesUtil.init("DECSECURITYKEYABCDEFG", "EiJPWIgQQDgoJXlRy91SZncpdZgwQEHi");
        for (int i = 0; i < list.size(); i++) {
            map = list.get(i);
            row = sheet.createRow((int) i + 1);
            HSSFCell datacell = null;
            Map.Entry<String, String> entry = null;
            int k = 0;
            for (Iterator<Map.Entry<String, String>> it = map.entrySet().iterator(); it
                    .hasNext();) {
                entry = it.next();
                datacell = row.createCell(k);
                if(entry.getKey().equals("strIdentity") || entry.getKey().equals("strMobile")){
                    datacell.setCellValue(aesUtil.decode(entry.getValue()));
                }else {
                    if(entry.getKey().equals("lBorrowIntentId")){
                        datacell.setCellValue(entry.getValue().toString());
                    }else{
                        datacell.setCellValue(entry.getValue());
                    }
                }
                k++;
            }
        }

        try {
            FileOutputStream fout = new FileOutputStream("D:\\ziptest\\zy_export_未结清.xls");
            wb.write(fout);
            String str = "导出成功！";
            System.out.println(str);
            fout.close();
        } catch (Exception e) {
            e.printStackTrace();
            String str1 = "导出失败！";
            System.out.println(str1);
        }
    }
}