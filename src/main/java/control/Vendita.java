package control;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.http.fileupload.servlet.ServletRequestContext;

import model.ProductBean;
import model.ProductModel;

/**
 * Servlet implementation class Vendita
 */
@WebServlet("/Vendita")
public class Vendita extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public Vendita() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * Sanitize input to prevent XSS
     * @param input the raw input string
     * @return the sanitized string
     */
    private String sanitizeInput(String input) {
        if (input == null) {
            return null;
        }
        return input.replaceAll("[<>\"'/]", "");
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ProductBean product = new ProductBean();
        product.setEmail(sanitizeInput((String) request.getSession().getAttribute("email")));

        String UPLOAD_DIRECTORY = request.getServletContext().getRealPath("/") + "img/productIMG/";
        // process only if its multipart content
        if (ServletFileUpload.isMultipartContent(request)) {
            try {
                List<FileItem> multiparts = new ServletFileUpload(
                        new DiskFileItemFactory()).parseRequest(new ServletRequestContext(request));

                for (FileItem item : multiparts) {
                    if (!item.isFormField()) {
                        String name = new File(item.getName()).getName();
                        item.write(new File(UPLOAD_DIRECTORY + File.separator + name));
                        product.setImmagine(sanitizeInput(name));
                    } else {
                        switch (item.getFieldName()) {
                            case "nome":
                                product.setNome(sanitizeInput(item.getString()));
                                break;
                            case "prezzo":
                                product.setPrezzo(Double.parseDouble(sanitizeInput(item.getString())));
                                break;
                            case "spedizione":
                                product.setSpedizione(Double.parseDouble(sanitizeInput(item.getString())));
                                break;
                            case "tipologia":
                                product.setTipologia(sanitizeInput(item.getString()));
                                break;
                            case "tag":
                                product.setTag(sanitizeInput(item.getString()));
                                break;
                            case "descrizione":
                                product.setDescrizione(sanitizeInput(item.getString()));
                                break;
                        }
                    }
                }

                // File uploaded successfully
                request.setAttribute("message", "File Uploaded Successfully");

            } catch (Exception ex) {
                request.setAttribute("message", "File Upload Failed due to " + ex);
            }
        } else {
            request.setAttribute("message", "Sorry, this Servlet only handles file upload request");
        }

        ProductModel model = new ProductModel();
        try {
            model.doSave(product);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        request.getSession().setAttribute("refreshProduct", true);
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }
}
