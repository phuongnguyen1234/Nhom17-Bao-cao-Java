package Control;

import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import View.*;
import Model.Product;

public class Control implements ActionListener {
    private View view;
    private JFileChooser fileChooser;
    private File file = null;
    private ArrayList<Product> list = new ArrayList<>();
    private boolean fileIsValid = true;

    public Control() {}

    public Control(View view, JFileChooser fileChooser) {
        this.view = view;
        this.fileChooser = fileChooser;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String source = e.getActionCommand();
        switch (source) {
            case "add":
                addProduct();
                break;

            case "edit":
                editProduct();
                break;

            case "delete":
                deleteProduct();
                break;

            case "search":
                searchProduct();
                break;

            case "sort":
                sortList();
                break;

            case "newFile":
                createNewFile();
                break;

            case "selectFile":
                chooseFile();
                break;

            default:
                break;
        }
    }

    private void addProduct() { //phuong thuc them mat hang 
        if (file == null) {
            JOptionPane.showMessageDialog(view.frame, "Chưa chọn file!");
            return;
        }
        
        // tao mot dialog de nhap thong tin mat hang moi
        JTextField idText = new JTextField();
        JTextField nameText = new JTextField();
        JTextField colorText = new JTextField();
        JTextField priceText = new JTextField();
        Object[] columnNames = { "Mã hàng:", idText, "Tên hàng:", nameText, "Màu sắc:", colorText, "Giá:", priceText };
    
        while (true) {
            int option = JOptionPane.showConfirmDialog(null, columnNames, "Thêm mặt hàng", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                String id = idText.getText();
                String name = nameText.getText();
                String color = colorText.getText();
                String priceInput = priceText.getText();
    
                if (id.isEmpty() || name.isEmpty() || color.isEmpty() || priceInput.isEmpty()) { //kiem tra cac thong tin da duoc nhap day du
                    JOptionPane.showMessageDialog(view.frame, "Vui lòng nhập đầy đủ thông tin!");
                    continue;
                }
    
                float price; //kiem tra thuoc tinh price co phai la gia tien hop le
                try {
                    price = Float.parseFloat(priceInput);
                    if(price <= 0) throw new NumberFormatException("Giá không hợp lệ! Vui lòng nhập lại.");
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(view.frame, "Giá không hợp lệ! Vui lòng nhập lại.");
                    continue;
                }
    
                // kiem tra neu ma hang moi da ton tai trong danh sach
                boolean isAdded = addToList(list, new Product(id, name, color, price));
                if (isAdded) {
                    JOptionPane.showMessageDialog(view.frame, "Thêm thành công");
                    view.updateTable(list);
                    putListIntoFile(list, file.getName());
                    break;
                } else {
                    JOptionPane.showMessageDialog(view.frame, "Mã hàng đã tồn tại! Vui lòng nhập lại.");
                }
            } else {
                break;
            }
        }
    }
    
    private void editProduct() { //phuong thuc chinh sua thong tin mat hang
        if (file == null) {
            JOptionPane.showMessageDialog(view.frame, "Chưa chọn file!");
            return;
        }
        
        //tao mot dialog de nhap ma hang can chinh sua
        String id = JOptionPane.showInputDialog(view.frame, "Nhập mã hàng cần sửa:");
        if (id == null) {
            return;
        }
    
        
        Product searchResult = search(list, id);    //kiem tra neu ma hang can tim co trong danh sach
        if (searchResult == null) {
            JOptionPane.showMessageDialog(view.frame, "Không tìm thấy sản phẩm với mã hàng này.");
            return;
        }
        
        JTextField nameText = new JTextField(searchResult.getName());
        JTextField colorText = new JTextField(searchResult.getColor());
        JTextField priceText = new JTextField(String.valueOf(searchResult.getPrice()));
    
        Object[] attributes = {
            "Tên hàng:", nameText,
            "Màu sắc:", colorText,
            "Giá:", priceText
        };
    
        while (true) {
            int option = JOptionPane.showConfirmDialog(null, attributes, "Chỉnh sửa mặt hàng", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                String name = nameText.getText();
                String color = colorText.getText();
                String priceInput = priceText.getText();
    
                if (name.isEmpty() || color.isEmpty() || priceInput.isEmpty()) { //kiem tra cac thong tin da duoc nhap day du
                    JOptionPane.showMessageDialog(view.frame, "Vui lòng nhập đầy đủ thông tin!");
                    continue;
                }
    
                float price; //kiem tra thuoc tinh price co phai la gia tien hop le
                try {
                    price = Float.parseFloat(priceInput);
                    if(price <= 0) throw new NumberFormatException("Giá không hợp lệ! Vui lòng nhập lại.");
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(view.frame, "Giá không hợp lệ! Vui lòng nhập lại.");
                    continue;
                }
    
                list.set(list.indexOf(searchResult), new Product(id, name, color, price));
                putListIntoFile(list, file.getName());
                view.updateTable(list);
                break;
            } else {
                break;
            }
        }
    }
    

    private void deleteProduct() { //phuong thuc xoa mat hang
        if (file == null) {
            JOptionPane.showMessageDialog(view.frame, "Chưa chọn file!");
            return;
        }

        //tao mot dialog de nhap ma hang can xoa
        String id = JOptionPane.showInputDialog(view.frame, "Nhập mã hàng cần xóa:");
        if (id == null) {
            return;
        }
        
        Product isFound = search(list, id); //kiem tra neu ma hang vua nhap co trong danh sach
        if (isFound == null) {
            JOptionPane.showMessageDialog(view.frame, "Không tìm thấy sản phẩm với mã hàng này.");
        } else {
            int choice = JOptionPane.showConfirmDialog(view.frame, "Bạn có chắc chắn muốn xóa sản phẩm này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                list.remove(isFound);
                putListIntoFile(list, file.getName());
                view.updateTable(list);
            }
        }
    }

    private void searchProduct() { //phuong thuc tim kiem mat hang
        if (file == null) {
            JOptionPane.showMessageDialog(view.frame, "Chưa chọn file!");
            return;
        }

        //tao mot dialog de nhap ma hang can tim
        String id = JOptionPane.showInputDialog(view.frame, "Nhập mã hàng cần tìm:");
        if (id == null) {
            return;
        }
        
        Product product = search(list, id);
        if (product == null) {
            JOptionPane.showMessageDialog(view.frame, "Không tìm thấy sản phẩm với mã hàng này.");
        } else {
            JOptionPane.showMessageDialog(view.frame, "Kết quả tìm kiếm:\n" +
                "Mã hàng: " + product.getId() + "\n" +
                "Tên hàng: " + product.getName() + "\n" +
                "Màu sắc: " + product.getColor() + "\n" +
                "Giá: " + product.getPrice());
        }
    }

    private void sortList() { //phuong thuc sap xep danh sach theo mot thuoc tinh
        if (file == null) {
            JOptionPane.showMessageDialog(view.frame, "Chưa chọn file!");
            return;
        }

        //tao mot dialog de chon thuoc tinh can sap xep
        String[] options = {"Mã hàng", "Tên hàng", "Màu sắc", "Giá"};
        int choice = JOptionPane.showOptionDialog(view.frame, "Chọn tiêu chí sắp xếp:",
            "Sắp xếp", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        Comparator<Product> comparator; 
        switch (choice) {
            case 0:
                comparator = Comparator.comparing(Product::getId);
                break;
            case 1:
                comparator = Comparator.comparing(Product::getName);
                break;
            case 2:
                comparator = Comparator.comparing(Product::getColor);
                break;
            case 3:
                comparator = Comparator.comparing(Product::getPrice);
                break;
            default:
                return;
        }
        list.sort(comparator);
        putListIntoFile(list, file.getName());
        view.updateTable(list);
    }

    private void createNewFile() { //phuong thuc tao file moi
        int result = fileChooser.showSaveDialog(view.frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            file = fileChooser.getSelectedFile();
            if (!file.getAbsolutePath().endsWith(".dat")) {
                file = new File(file.getAbsolutePath() + ".dat");
            }
            try {
                if (file.createNewFile()) {
                    view.fileIsChosen = true;
                    list.clear();
                    view.updateTable(list);
                    view.frame.setTitle("Quản lí mặt hàng - " + file.getAbsolutePath() + " (lưu tự động)");
                } else {
                    JOptionPane.showMessageDialog(view.frame, "File đã tồn tại!");
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(view.frame, "Lỗi khi tạo file mới: " + e.getMessage());
            }
        }
    }
    

    private void chooseFile() { //phuong thuc chon file
        int result = fileChooser.showOpenDialog(view.frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            file = fileChooser.getSelectedFile();
            list = getListFromFile(file.getName());
            if(fileIsValid) {
                view.updateTable(list);
                view.fileIsChosen = true;
                view.frame.setTitle("Quản lí mặt hàng - " + file.getAbsolutePath() + " (lưu tự động)");
            }
        }
    }

    public ArrayList<Product> getListFromFile(String fileName) { //phuong thuc doc danh sach tu file
        ArrayList<Product> list = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            list = (ArrayList<Product>) ois.readObject();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view.frame, "File không hợp lệ!");
            fileIsValid = false;
        }
        return list;
    }

    public void putListIntoFile(ArrayList<Product> list, String fileName) { //phuong thuc ghi danh sach vao file
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(list);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(view.frame, "Lỗi ghi file!");
        }
    }

    public boolean addToList(ArrayList<Product> list, Product p) { //phuong thuc them mat hang vao danh sach
        if(!ifIDExists(list, p.getId())){
            list.add(p);
            return true;
        }
        else return false;
    }

    public Product search(ArrayList<Product> list, String id) { //phuong thuc tim kiem mat hang trong danh sach
        for (Product product : list) {
            if (product.getId().equals(id)) {
                return product;
            }
        }
        return null;
    }

    public boolean ifIDExists(ArrayList<Product> list, String id){ //phuong thuc kiem tra ma hang vua nhap co trong danh sach
        for (Product product : list) {
            if (product.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }
}
