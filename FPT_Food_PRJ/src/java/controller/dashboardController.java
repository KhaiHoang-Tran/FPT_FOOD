/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.CategoryDAO;
import dal.DiningTableDAO;
import dal.FoodDAO;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Category;
import model.DiningTable;
import model.Food;
import model.User;

/**
 *
 * @author Nitro 5 Tiger
 */
@WebServlet(name = "dashboardController", urlPatterns = {"/dashboardController"})
public class dashboardController extends HttpServlet {

    protected void processRequest(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        String action = request.getParameter("action");

        if (action != null) {

            switch (action) {

                case "updateTable":
                    updateTable(request, response);
                    return;

                case "insertCategory":
                case "updateCategory":
                case "deleteCategory":
                    handleCategory(request, response);
                    return;

                case "addFood":
                case "updateFood":
                case "deleteFood":
                    handleFood(request, response);
                    return;
            }
        }

        // ===== LOAD DASHBOARD =====
        loadDashboard(request, response);
    }

    private void loadDashboard(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String tab = request.getParameter("tab");
        if (tab == null) {
            tab = "tables";
        }

        request.setAttribute("activeTab", tab);

        DiningTableDAO tableDAO = new DiningTableDAO();
        CategoryDAO categoryDAO = new CategoryDAO();
        FoodDAO foodDAO = new FoodDAO();

        switch (tab) {

            case "tables":

                List<DiningTable> tables = tableDAO.getAll();

                for (DiningTable t : tables) {
                    String status = t.getStatus();

//                    int activeOrder = tableDAO.countActiveOrders(t.getTableID());
//
//                    if (activeOrder > 0) {
//
//                        int count = tableDAO.getServingItemCount(t.getTableID());
//                        double total = tableDAO.getTotalByTable(t.getTableID());
//
//                        t.setServingCount(count);
//                        t.setCurrentTotal(total);
                    if ("busy".equals(status)) {

                        int count = tableDAO.getServingItemCount(t.getTableID());
                        double total = tableDAO.getTotalByTable(t.getTableID());

                        t.setServingCount(count);
                        t.setCurrentTotal(total);

                    } else {

                        t.setServingCount(0);
                        t.setCurrentTotal(0.0);
                    }
                }

                request.setAttribute("tables", tables);
                break;

            case "categories":
                request.setAttribute("categories", categoryDAO.getAll());
                break;

            case "food":
                request.setAttribute("foods", foodDAO.getAllForAdmin());
                request.setAttribute("categories", categoryDAO.getAll());
                break;
        }

        request.getRequestDispatcher("dashboard.jsp").forward(request, response);
    }

    // ===== TABLE =====
    private void updateTable(HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        int tableId = Integer.parseInt(request.getParameter("tableId"));
        String status = request.getParameter("status");

        DiningTableDAO dao = new DiningTableDAO();
        dao.updateStatus(tableId, status);

        response.sendRedirect("dashboardController?tab=tables");
    }

    // ===== CATEGORY =====
    private void handleCategory(HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        String action = request.getParameter("action");

        CategoryDAO dao = new CategoryDAO();

        if ("insertCategory".equals(action)) {

            Category c = new Category();
            c.setName(request.getParameter("name"));
            c.setDescription(request.getParameter("description"));

            dao.insert(c);

        } else if ("updateCategory".equals(action)) {

            Category c = new Category();
            c.setCategoryID(Integer.parseInt(request.getParameter("id")));
            c.setName(request.getParameter("name"));
            c.setDescription(request.getParameter("description"));

            dao.update(c);

        } else if ("deleteCategory".equals(action)) {

            int id = Integer.parseInt(request.getParameter("id"));
            dao.delete(id);
        }

        response.sendRedirect("dashboardController?tab=categories");
    }

    // ===== FOOD =====
    private void handleFood(HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        String action = request.getParameter("action");

        FoodDAO dao = new FoodDAO();

        if ("addFood".equals(action)) {

            Food f = new Food();

            f.setName(request.getParameter("name"));
            f.setPrice(Double.parseDouble(request.getParameter("price")));
            f.setCategoryID(Integer.parseInt(request.getParameter("categoryID")));
            f.setStatus(request.getParameter("status"));

            dao.insertFoodAdmin(f);

        } else if ("updateFood".equals(action)) {

            Food f = new Food();

            f.setFoodID(Integer.parseInt(request.getParameter("foodID")));
            f.setName(request.getParameter("name"));
            f.setPrice(Double.parseDouble(request.getParameter("price")));
            f.setCategoryID(Integer.parseInt(request.getParameter("categoryID")));
            f.setStatus(request.getParameter("status"));

            dao.updateFoodAdmin(f);

        } else if ("deleteFood".equals(action)) {

            int id = Integer.parseInt(request.getParameter("foodID"));
            dao.deleteFoodAdmin(id);
        }

        response.sendRedirect("dashboardController?tab=food");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

}
