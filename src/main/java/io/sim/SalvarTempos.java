package io.sim;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class SalvarTempos {   
    private static final String fileName = "C:/Users/rribe/OneDrive/Documentos/UFLA/2023 02/Avançada/projeto parte 2/inicial/sim-main/src/main/java/io/sim/tempos.xls";
    
    private static HSSFWorkbook workbook = new HSSFWorkbook();
    private static HSSFSheet sheet = workbook.createSheet("Relatório dos Carros");              
    private static int rownum = 0;

    public static void adicionarTemposDeRota(ArrayList<Long> registroDeTempos) {
        Row row = sheet.createRow(rownum++);
        int cellnum = 0;
        Cell currentCell;

        for (int i=0;i<registroDeTempos.size();i++){
            currentCell = row.createCell(cellnum++);
            currentCell.setCellValue(registroDeTempos.get(i));
        }
  
        try {
            synchronized (workbook){
                FileOutputStream out = new FileOutputStream(new File(SalvarTempos.fileName));
                workbook.write(out);
                out.close();      
            }     
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Arquivo não encontrado!");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erro na edição do arquivo!");
        }
    }
}
