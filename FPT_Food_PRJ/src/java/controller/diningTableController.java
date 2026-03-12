/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.DiningTableDAO;
import dal.OrdersDAO;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.DiningTable;

/**
 *
 * @author AN
 */
@WebServlet(name = "diningTableController", urlPatterns = {"/diningTableController"})
public class diningTableController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        String action = request.getParameter("action");
        String activeTab = "bills";
        request.setAttribute("activeTab", activeTab);
        switch (action) {
            case "pay":
                doPay(request, response);
                break;
        }
        doBills(request, response);
        request.getRequestDispatcher("dashboard.jsp").forward(request, response);
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private void doBills(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        DiningTableDAO dDAO = new DiningTableDAO();
        Map<DiningTable, Double> listBills = new HashMap<>();
        listBills.putAll(dDAO.getDiningTableAndFinalPrice());
        request.setAttribute("listBills", listBills);
    }

    private void doPay(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        int tableId = Integer.parseInt(request.getParameter("tableId"));
        DiningTableDAO dDAO = new DiningTableDAO();
//        int check = dDAO.updateStatusTable(tableId);
//        if (check >= 0) {
//            System.out.println("So dong da duoc cap nhat la " + check);
//        } else {
//            System.out.println("Error");
//        }

        OrdersDAO odao = new OrdersDAO();

        // Lấy order hiện tại
        Integer orderID = (Integer) session.getAttribute("orderID");

        if (orderID != null) {

            // cập nhật order → paid
            odao.updateStatusOrder(orderID, "paid");

            // xóa order khỏi session
            session.removeAttribute("orderID");
        }

        // bàn → empty
        int check = dDAO.updateStatusTable(tableId);

        if (check >= 0) {
            System.out.println("So dong da duoc cap nhat la " + check);
        } else {
            System.out.println("Error");
        }

        // user rời bàn
        session.removeAttribute("currentTable");
    }

}
