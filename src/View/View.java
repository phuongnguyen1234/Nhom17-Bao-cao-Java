package View;

import javax.swing.*;
import javax.swing.table.*;
import java.util.ArrayList;
import Control.*;
import Model.Product;

public class View {
    private JTable table;
    private DefaultTableModel model;
    private JFileChooser fileChooser = new JFileChooser();
    public JFrame frame = new JFrame("Quản lí mặt hàng");
    public boolean fileIsChosen = false;
    Control control = new Control(this, fileChooser);

    public void createAndShowUI() {
        // Tao cua so chuong trinh
        frame.setSize(800, 500);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Tao thanh menu
        JMenuBar menuBar = new JMenuBar();
        JMenu selectFileMenu = new JMenu("File");
        JMenuItem newFile = new JMenuItem("Tạo file mới");
        JMenuItem selectFile = new JMenuItem("Chọn File");

        selectFileMenu.add(newFile);
        selectFileMenu.add(selectFile);
        menuBar.add(selectFileMenu);
        frame.setJMenuBar(menuBar);

        newFile.setActionCommand("newFile");
        selectFile.setActionCommand("selectFile");

        newFile.addActionListener(control);
        selectFile.addActionListener(control);

        // Tao panel chua cac thanh phan
        JPanel panel = new JPanel();
        frame.add(panel);
        placeComponents(panel);
        frame.setVisible(true);
    }

    private void placeComponents(JPanel panel) { //phuong thuc them cac thanh phan vao cua so
        panel.setLayout(null);

        // Them cac nut chuc nang
        JButton addButton = new JButton("Thêm mặt hàng");
        addButton.setActionCommand("add");
        addButton.setBounds(550, 50, 200, 35);
        panel.add(addButton);

        JButton editButton = new JButton("Sửa mặt hàng");
        editButton.setActionCommand("edit");
        editButton.setBounds(550, 100, 200, 35);
        panel.add(editButton);

        JButton deleteButton = new JButton("Xóa mặt hàng");
        deleteButton.setActionCommand("delete");
        deleteButton.setBounds(550, 150, 200, 35);
        panel.add(deleteButton);

        JButton searchButton = new JButton("Tìm kiếm mặt hàng");
        searchButton.setActionCommand("search");
        searchButton.setBounds(550, 200, 200, 35);
        panel.add(searchButton);

        JButton sortButton = new JButton("Sắp xếp danh sách");
        sortButton.setActionCommand("sort");
        sortButton.setBounds(550, 250, 200, 35);
        panel.add(sortButton);

        //Them cac ActionListener cho cac nut
        addButton.addActionListener(control);
        editButton.addActionListener(control);
        deleteButton.addActionListener(control);
        searchButton.addActionListener(control);
        sortButton.addActionListener(control);

        //Tao bang hien thi thong tin danh sach
        String[] columnNames = {"STT", "Mã hàng", "Tên hàng", "Màu sắc", "Giá"};
        model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(model);
        table.getTableHeader().setReorderingAllowed(false);

        TableColumnModel columnModel = table.getColumnModel();
        int columnCount = columnModel.getColumnCount();
        for (int i = 0; i < columnCount; i++) {
            TableColumn column = columnModel.getColumn(i);
            column.setResizable(false);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(20, 20, 500, 400);
        panel.add(scrollPane);
    }

    public void updateTable(ArrayList<Product> products) { //Phuong thuc cap nhat bang
        model.setRowCount(0);
        int i = 1;
        for (Product product : products) {
            model.addRow(new Object[]{
                i,
                product.getId(),
                product.getName(),
                product.getColor(),
                product.getPrice()
            });
            i++;
        }
    }
}
