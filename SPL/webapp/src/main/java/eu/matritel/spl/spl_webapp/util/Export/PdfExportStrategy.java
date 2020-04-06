package eu.matritel.spl.spl_webapp.util.Export;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import eu.matritel.spl.spl_webapp.entity.CheckList;
import eu.matritel.spl.spl_webapp.entity.Project;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Optional;


@Component
public class PdfExportStrategy implements ExportStrategy {
    private Paragraph setHeading() {
        int alignment = 1;
        float spacingAfter = 15f;
        int fontSize = 20;
        String headingName = "matritellabs";

        Font f1 = FontFactory.getFont(FontFactory.TIMES_ROMAN, fontSize);
        Paragraph h = new Paragraph();
        h.setAlignment(alignment);

        Chunk c = new Chunk();
        c.setUnderline(1.5f, -1.5f);
        c.setFont(f1);
        c.append(headingName.toUpperCase());
        h.add(c);
        h.setSpacingAfter(spacingAfter);

        return h;
    }

    private List setProjectList(Optional<Project> project) {
        List list = new List(List.ORDERED);
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

        project.ifPresent(pt -> {
            list.add(new ListItem("Name of project: " + pt.getName()));
            if(pt.getSite() != null) {
                list.add(new ListItem("Site: " + pt.getSite().getName()));
            } else {
                list.add(new ListItem("Site: - " ));
            }

            list.add(new ListItem("Address : " + pt.getAddress()));
            list.add(new ListItem("Name of Auditor: " + pt.getAuditor().getName()));
            list.add(new ListItem("Name of Engineer: " + pt.getEngineer().getName()));

            String dateString = pt.getDate() != null ? format.format(pt.getDate()) : "N/A";
            list.add(new ListItem("Date: " + dateString));
        });

        return list;
    }

    private Paragraph setSeparator() {
        Paragraph s = new Paragraph(new Chunk(new LineSeparator()));
        return s;
    }

    private PdfPTable setChecklistTable(java.util.List<CheckList> checkLists) throws DocumentException {
        int numOfColumns = 4;
        int widthPercentage = 100;
        float spacingBefore = 15f;
        float spacingAfter = 0f;
        float[] columnWidths = {0.3f, 1.5f, 0.3f, 1f};

        PdfPTable table = new PdfPTable(numOfColumns);
        table.setWidthPercentage(widthPercentage);
        table.setWidths(columnWidths);
        table.setSpacingBefore(spacingBefore);
        table.setSpacingAfter(spacingAfter);

        // Create first row of table
        String[] firstRow = {"IN", "Description", "Status", "Comment"};
        for (String s : firstRow) {
            PdfPCell cell = new PdfPCell();
            Paragraph p = new Paragraph(s);
            p.setAlignment(1);
            cell.addElement(p);
            cell.setBackgroundColor(BaseColor.GREEN);
            table.addCell(cell);
        }

        // Rest of the table
        for (CheckList ch : checkLists) {
            table.addCell(ch.getItemNumber());
            table.addCell(ch.getDescription());
            table.addCell(ch.getStatus());
            table.addCell(ch.getComment());
        }

        return table;
    }

    private String getProjectName(Optional<Project> project) {
        final String[] name = {null};
        project.ifPresent(pt -> {
            name[0] = pt.getName();
        });

        return name[0];
    }

    @Override
    public String export(String path, Optional<Project> project, java.util.List<CheckList> checkLists) {
        Document document = new Document();
        String projectName = getProjectName(project);
        String fileName = null;

        try {
            Paragraph heading = setHeading();
            List projectList = setProjectList(project);
            Paragraph separator = setSeparator();
            PdfPTable table = setChecklistTable(checkLists);
            fileName = path+"/"+encodeFilename(projectName)+".pdf";
            File f = new File(fileName);
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(f));

            document.open();
            document.add(heading);
            document.add(projectList);
            document.add(separator);
            document.add(table);
            document.close();

            writer.close();

        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return fileName;
    }

    public static String encodeFilename(String s)
    {
        try
        {
            return "myApp-" + java.net.URLEncoder.encode(s, "UTF-8");
        }
        catch (java.io.UnsupportedEncodingException e)
        {
            throw new RuntimeException("UTF-8 is an unknown encoding!?");
        }
    }
}