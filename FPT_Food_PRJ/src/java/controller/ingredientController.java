/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.IngredientDAO;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Ingredient;

/**
 *
 * @author AN
 */
public class ingredientController extends HttpServlet {

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
        action = action == null ? "" : request.getParameter("action");
        switch (action) {
            case "deleteIngredient":
                deleteIngredient(request, response);
                break;
            case "addIngredient":
                doAdd(request, response);
                break;
        }
        displayIngredient(request, response);

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

    private void displayIngredient(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        String from = request.getParameter("from");
        IngredientDAO iDAO = new IngredientDAO();
        ArrayList<Ingredient> listIngredient = (ArrayList<Ingredient>) iDAO.getAll();
        setRequest(request, response, listIngredient, from);
    }

    private void setRequest(HttpServletRequest request, HttpServletResponse response, ArrayList<Ingredient> listIngredient, String from) throws ServletException, IOException {
        switch (from) {
            case "kitchen":
                String mesg = "";
                String activeSection = "";
                if (listIngredient.size() <= 0) {
                    mesg += "Kho rỗng, không có nguyên liệu tồn kho";
                    request.setAttribute("mesg", mesg);
                    request.getRequestDispatcher("kitchen.jsp").forward(request, response);
                } else {
                    request.setAttribute("listIngredient", listIngredient);
                    activeSection += "inventory";
                    request.setAttribute("activeSection", activeSection);
                    request.getRequestDispatcher("kitchen.jsp").forward(request, response);
                }
                break;
            case "dashboard":
                request.setAttribute("listIngredient", listIngredient);
                request.setAttribute("activeTab", "inventory");
                request.getRequestDispatcher("dashboard.jsp").forward(request, response);
                break;
        }
    }

    private void deleteIngredient(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        int id = Integer.parseInt(request.getParameter("id"));
        IngredientDAO iDAO = new IngredientDAO();
        int check = iDAO.deleteByID(id);
        if (check >= 0) {
            System.out.println("So dong da duoc cap nhat la " + check);
        } else {
            System.out.println("Error");
        }
    }

    private void doAdd(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        String name = request.getParameter("name");
        Double qyt = Double.parseDouble(request.getParameter("qty"));
        Ingredient i = Ingredient.builder()
                .name(name)
                .quantityInStock(qyt).build();
        IngredientDAO iDAO = new IngredientDAO();
        int check = iDAO.insert(i);
        if (check >= 0) {
            System.out.println("So dong da duoc cap nhat la " + check);
        } else {
            System.out.println("Error");
        }
    }
}
