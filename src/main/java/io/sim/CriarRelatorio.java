package io.sim;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
  
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class CriarRelatorio {   
    private static final String fileName = "C:/Users/rribe/OneDrive/Documentos/UFLA/2023 02/Avançada/projeto parte 2/inicial/sim-main/src/main/java/io/sim/novo.xls";
    
    private static HSSFWorkbook workbook = new HSSFWorkbook();
    private static HSSFSheet sheet = workbook.createSheet("Relatório dos Carros");              
    private static int rownum = 0;

    public static void inicializarPlanilha(){
        Row row = sheet.createRow(rownum++);
        int cellnum = 0;
        Cell currentCell = row.createCell(cellnum++);
        currentCell.setCellValue("Timestamp");
        
        currentCell = row.createCell(cellnum++);
        currentCell.setCellValue("ID Car");
        
        currentCell = row.createCell(cellnum++);
        currentCell.setCellValue("ID Route");
        
        currentCell = row.createCell(cellnum++);
        currentCell.setCellValue("Speed");
        
        currentCell = row.createCell(cellnum++);
        currentCell.setCellValue("Distance");
        
        currentCell = row.createCell(cellnum++);
        currentCell.setCellValue("FuelConsumption");
        
        currentCell = row.createCell(cellnum++);
        currentCell.setCellValue("FuelType");
        
        currentCell = row.createCell(cellnum++);
        currentCell.setCellValue("CO2Emission");
        
        currentCell = row.createCell(cellnum++);
        currentCell.setCellValue("longitude (lon)");

        currentCell = row.createCell(cellnum++);
        currentCell.setCellValue("latitude (lat)");

        currentCell = row.createCell(cellnum++);
        currentCell.setCellValue("ID Edge");
    }

    public static void adicionarAoRelatorio(DadosRelatorio dadosRelatorio) {
        Row row = sheet.createRow(rownum++);
        int cellnum = 0;
        Cell currentCell = row.createCell(cellnum++);
        currentCell.setCellValue(dadosRelatorio.timestamp);
        
        currentCell = row.createCell(cellnum++);
        currentCell.setCellValue(dadosRelatorio.idCar);
        
        currentCell = row.createCell(cellnum++);
        currentCell.setCellValue(dadosRelatorio.idRoute);
        
        currentCell = row.createCell(cellnum++);
        currentCell.setCellValue(dadosRelatorio.speed);
        
        currentCell = row.createCell(cellnum++);
        currentCell.setCellValue(dadosRelatorio.distance);
        
        currentCell = row.createCell(cellnum++);
        currentCell.setCellValue(dadosRelatorio.fuelConsumption);
        
        currentCell = row.createCell(cellnum++);
        currentCell.setCellValue(dadosRelatorio.fuelType);
        
        currentCell = row.createCell(cellnum++);
        currentCell.setCellValue(dadosRelatorio.CO2Emission);
        
        currentCell = row.createCell(cellnum++);
        currentCell.setCellValue(dadosRelatorio.longitude);

        currentCell = row.createCell(cellnum++);
        currentCell.setCellValue(dadosRelatorio.latitude);

        currentCell = row.createCell(cellnum++);
        currentCell.setCellValue(dadosRelatorio.idRoad);
  
        try {
            // synchronized (workbook){
                FileOutputStream out = new FileOutputStream(new File(CriarRelatorio.fileName));
                workbook.write(out);
                out.close();      
            // }     
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Arquivo não encontrado!");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erro na edição do arquivo!");
        }
    }
}