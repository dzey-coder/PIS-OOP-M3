package com.example.ims001;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.nio.file.*;
import java.util.Comparator;
import java.util.UUID;

public class InventoryView {

    private final MainApp mainApp;
    private final BorderPane root = new BorderPane();

    private final TableView<Product> table = new TableView<>();
    private final ObservableList<Product> masterData = FXCollections.observableArrayList();

    private FilteredList<Product> filtered;
    private SortedList<Product> sorted;

    private final TextField tfName = new TextField();
    private final TextField tfCategory = new TextField();
    private final TextField tfQty = new TextField();
    private final TextField tfPrice = new TextField();

    private final Label status = new Label();
    private final Label stockWarning = new Label();

    private final TextField tfSearch = new TextField();
    private boolean lowStockFirstEnabled = false;
    private boolean warnedOnce = false;

    private String selectedImagePath = null;

    public InventoryView(MainApp mainApp) {
        this.mainApp = mainApp;
        buildUI();
        setupFilterAndSort();
        refresh();
    }

    private void buildUI() {
        // Root background (modern light)
        root.setStyle("-fx-background-color: #F5F7FB;");

        // Top bar
        Button btnBack = new Button("Back to Dashboard");
        btnBack.setOnAction(e -> mainApp.showDashboardView(Session.getUsername()));

        Button btnRefresh = new Button("Refresh");
        btnRefresh.setOnAction(e -> refresh());

        tfSearch.setPromptText("Search (name / category / status)...");
        tfSearch.setPrefWidth(380);

        Button btnLowStockFirst = new Button("Low Stock First: OFF");
        btnLowStockFirst.setOnAction(e -> {
            lowStockFirstEnabled = !lowStockFirstEnabled;
            btnLowStockFirst.setText(lowStockFirstEnabled ? "Low Stock First: ON" : "Low Stock First: OFF");
            applySort();
        });

        // Stock warning label style baseline
        stockWarning.setStyle("-fx-font-weight: bold; -fx-padding: 0 0 0 8;");

        HBox topBar = new HBox(10, btnBack, btnRefresh, tfSearch, btnLowStockFirst, stockWarning);
        topBar.setAlignment(Pos.CENTER_LEFT);

        // Put top bar in a "card"
        HBox topCard = new HBox(topBar);
        topCard.setPadding(new Insets(12));
        topCard.setStyle("""
                -fx-background-color: white;
                -fx-background-radius: 14;
                -fx-border-radius: 14;
                -fx-border-color: #E6EAF2;
                -fx-border-width: 1;
                """);

        VBox topWrap = new VBox(12, topCard);
        topWrap.setPadding(new Insets(14, 14, 10, 14));

        // Table modern look
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setStyle("""
                -fx-background-color: white;
                -fx-background-radius: 14;
                -fx-border-radius: 14;
                -fx-border-color: #E6EAF2;
                -fx-border-width: 1;
                """);

        // Bigger rows so bigger images look good
        table.setFixedCellSize(74); // row height

        // Columns
        TableColumn<Product, Integer> colId = new TableColumn<>("ID");
        colId.setMaxWidth(1f * Integer.MAX_VALUE);
        colId.setCellValueFactory(c ->
                new javafx.beans.property.SimpleIntegerProperty(c.getValue().getId()).asObject()
        );

        // ✅ Image column BETWEEN ID and Name, bigger image
        TableColumn<Product, Integer> colImg = new TableColumn<>("Image");
        colImg.setMaxWidth(1f * Integer.MAX_VALUE);
        colImg.setCellValueFactory(c ->
                new javafx.beans.property.SimpleIntegerProperty(c.getValue().getId()).asObject()
        );

        colImg.setCellFactory(col -> new TableCell<>() {
            private final ImageView iv = new ImageView();

            {
                iv.setFitWidth(128);
                iv.setFitHeight(128);
                iv.setPreserveRatio(false);
                iv.setSmooth(true);

                setAlignment(Pos.CENTER);
            }

            @Override
            protected void updateItem(Integer productId, boolean empty) {
                super.updateItem(productId, empty);

                if (empty || productId == null) {
                    setGraphic(null);
                    return;
                }

                String path = ProductImageDAO.getImagePath(productId);
                if (path == null || path.isBlank()) {
                    setGraphic(null);
                    return;
                }

                try {
                    iv.setImage(new javafx.scene.image.Image("file:" + path, true));
                    setGraphic(iv);
                } catch (Exception e) {
                    setGraphic(null);
                }
            }
        });

        TableColumn<Product, String> colName = new TableColumn<>("Name");
        colName.setMaxWidth(1f * Integer.MAX_VALUE);
        colName.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().getName())
        );

        TableColumn<Product, String> colCat = new TableColumn<>("Category");
        colCat.setMaxWidth(1f * Integer.MAX_VALUE);
        colCat.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().getCategory())
        );

        TableColumn<Product, Integer> colQty = new TableColumn<>("Qty");
        colQty.setMaxWidth(1f * Integer.MAX_VALUE);
        colQty.setCellValueFactory(c ->
                new javafx.beans.property.SimpleIntegerProperty(c.getValue().getQuantity()).asObject()
        );

        TableColumn<Product, Double> colPrice = new TableColumn<>("Price");
        colPrice.setMaxWidth(1f * Integer.MAX_VALUE);
        colPrice.setCellValueFactory(c ->
                new javafx.beans.property.SimpleDoubleProperty(c.getValue().getPrice()).asObject()
        );

        TableColumn<Product, String> colStatus = new TableColumn<>("Status");
        colStatus.setMaxWidth(1f * Integer.MAX_VALUE);
        colStatus.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().getStatus())
        );

        colStatus.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String s, boolean empty) {
                super.updateItem(s, empty);
                if (empty || s == null) {
                    setText(null);
                    setStyle("");
                    return;
                }
                setText(s);
                switch (s) {
                    case "OUT OF STOCK" -> setStyle("-fx-text-fill: #E11D48; -fx-font-weight: bold;");
                    case "LOW STOCK" -> setStyle("-fx-text-fill: #F59E0B; -fx-font-weight: bold;");
                    case "IN STOCK" -> setStyle("-fx-text-fill: #10B981; -fx-font-weight: bold;");
                    default -> setStyle("");
                }
            }
        });

        // ✅ New order: ID, Image, Name, Category, Qty, Price, Status
        table.getColumns().setAll(colId, colImg, colName, colCat, colQty, colPrice, colStatus);

        table.getSelectionModel().selectedItemProperty().addListener((obs, old, sel) -> {
            if (sel != null) {
                tfName.setText(sel.getName());
                tfCategory.setText(sel.getCategory());
                tfQty.setText(String.valueOf(sel.getQuantity()));
                tfPrice.setText(String.valueOf(sel.getPrice()));
                selectedImagePath = ProductImageDAO.getImagePath(sel.getId());
            }
        });

        // Form inputs (slightly modern)
        tfName.setPromptText("Name");
        tfCategory.setPromptText("Category");
        tfQty.setPromptText("Quantity");
        tfPrice.setPromptText("Price");

        styleInput(tfName);
        styleInput(tfCategory);
        styleInput(tfQty);
        styleInput(tfPrice);
        styleInput(tfSearch);

        // Buttons
        Button btnAdd = new Button("Add Product");
        btnAdd.setOnAction(e -> add());

        Button btnEdit = new Button("Edit Selected");
        btnEdit.setOnAction(e -> editSelected());

        Button btnRemove = new Button("Remove Selected");
        btnRemove.setOnAction(e -> removeSelected());

        Button btnChooseImage = new Button("Choose Image");
        btnChooseImage.setOnAction(e -> chooseImage());

        stylePrimary(btnAdd);
        stylePrimary(btnEdit);
        styleDanger(btnRemove);
        styleSecondary(btnChooseImage);
        styleSecondary(btnBack);
        styleSecondary(btnRefresh);
        styleSecondary(btnLowStockFirst);

        // Form layout card
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.addRow(0, new Label("Name"), tfName);
        form.addRow(1, new Label("Category"), tfCategory);
        form.addRow(2, new Label("Qty"), tfQty);
        form.addRow(3, new Label("Price"), tfPrice);

        // Make fields expand
        ColumnConstraints c1 = new ColumnConstraints();
        c1.setMinWidth(80);
        ColumnConstraints c2 = new ColumnConstraints();
        c2.setHgrow(Priority.ALWAYS);
        form.getColumnConstraints().setAll(c1, c2);

        HBox actions = new HBox(10, btnAdd, btnEdit, btnRemove, btnChooseImage);
        actions.setAlignment(Pos.CENTER_LEFT);

        status.setStyle("-fx-text-fill: #374151; -fx-padding: 6 0 0 0;");

        VBox bottomCard = new VBox(10, form, actions, status);
        bottomCard.setPadding(new Insets(14));
        bottomCard.setStyle("""
                -fx-background-color: white;
                -fx-background-radius: 14;
                -fx-border-radius: 14;
                -fx-border-color: #E6EAF2;
                -fx-border-width: 1;
                """);

        VBox bottomWrap = new VBox(bottomCard);
        bottomWrap.setPadding(new Insets(10, 14, 14, 14));

        // Center wrap to give padding and card look
        VBox centerWrap = new VBox(table);
        centerWrap.setPadding(new Insets(0, 14, 0, 14));

        root.setTop(topWrap);
        root.setCenter(centerWrap);
        root.setBottom(bottomWrap);
    }

    private void styleInput(TextField tf) {
        tf.setStyle("""
                -fx-background-color: #FFFFFF;
                -fx-background-radius: 10;
                -fx-border-radius: 10;
                -fx-border-color: #E6EAF2;
                -fx-border-width: 1;
                -fx-padding: 8 10 8 10;
                """);
    }

    private void stylePrimary(Button b) {
        b.setStyle("""
                -fx-background-color: #2563EB;
                -fx-text-fill: white;
                -fx-font-weight: bold;
                -fx-background-radius: 10;
                -fx-padding: 8 12 8 12;
                """);
    }

    private void styleSecondary(Button b) {
        b.setStyle("""
                -fx-background-color: #EEF2FF;
                -fx-text-fill: #1F2937;
                -fx-font-weight: bold;
                -fx-background-radius: 10;
                -fx-padding: 8 12 8 12;
                """);
    }

    private void styleDanger(Button b) {
        b.setStyle("""
                -fx-background-color: #FEE2E2;
                -fx-text-fill: #991B1B;
                -fx-font-weight: bold;
                -fx-background-radius: 10;
                -fx-padding: 8 12 8 12;
                """);
    }

    private void setupFilterAndSort() {
        filtered = new FilteredList<>(masterData, p -> true);
        sorted = new SortedList<>(filtered);

        sorted.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sorted);

        tfSearch.textProperty().addListener((obs, old, text) -> applyFilter(text));
    }

    private void applyFilter(String text) {
        String q = (text == null) ? "" : text.trim().toLowerCase();

        filtered.setPredicate(p -> {
            if (q.isEmpty()) return true;

            String name = safe(p.getName());
            String cat = safe(p.getCategory());
            String status = safe(p.getStatus());

            return name.contains(q) || cat.contains(q) || status.contains(q);
        });
    }

    private String safe(String s) {
        return s == null ? "" : s.toLowerCase();
    }

    private void applySort() {
        if (!lowStockFirstEnabled) {
            sorted.comparatorProperty().bind(table.comparatorProperty());
            table.sort();
            return;
        }

        Comparator<Product> cmp = Comparator
                .comparingInt(this::statusRank)
                .thenComparingInt(Product::getQuantity)
                .thenComparing(p -> p.getName() == null ? "" : p.getName().toLowerCase());

        sorted.comparatorProperty().unbind();
        sorted.setComparator(cmp);
        table.sort();
    }

    private int statusRank(Product p) {
        int q = p.getQuantity();
        if (q == 0) return 0;
        if (q <= 10) return 1;
        return 2;
    }

    private void refresh() {
        masterData.setAll(ProductDAO.getAll());

        int low = 0, out = 0;
        for (Product p : masterData) {
            if (p.getQuantity() == 0) out++;
            else if (p.getQuantity() <= 10) low++;
        }

        status.setText("Loaded " + masterData.size() + " products.");

        if (out > 0 || low > 0) {
            stockWarning.setText("⚠ Low: " + low + " | Out: " + out);
            stockWarning.setStyle("-fx-text-fill: #F59E0B; -fx-font-weight: bold; -fx-padding: 0 0 0 8;");
        } else {
            stockWarning.setText("All stock healthy ✅");
            stockWarning.setStyle("-fx-text-fill: #10B981; -fx-font-weight: bold; -fx-padding: 0 0 0 8;");
        }

        applyFilter(tfSearch.getText());
        applySort();

        if (!warnedOnce && (out > 0 || low > 0)) {
            warnedOnce = true;
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Stock Alert");
            alert.setHeaderText("Stock warning detected");
            alert.setContentText("Low stock items: " + low + "\nOut of stock items: " + out);
            alert.showAndWait();
        }
    }

    private void add() {
        try {
            String name = tfName.getText().trim();
            String cat = tfCategory.getText().trim();
            int qty = Integer.parseInt(tfQty.getText().trim());
            double price = Double.parseDouble(tfPrice.getText().trim());

            if (name.isEmpty() || cat.isEmpty()) {
                status.setText("Name and Category are required.");
                return;
            }

            boolean ok = ProductDAO.add(name, cat, qty, price);
            if (ok) {
                String tempStatus = new Product(0, name, cat, qty, price).getStatus();
                HistoryDAO.log("ADD", name, "Added (" + qty + " pcs) | Status: " + tempStatus);
            }

            status.setText(ok ? "Product added." : "Add failed.");
            selectedImagePath = null;
            refresh();
        } catch (Exception ex) {
            status.setText("Invalid input (qty/price must be numbers).");
        }
    }

    private void editSelected() {
        Product sel = table.getSelectionModel().getSelectedItem();
        if (sel == null) {
            status.setText("Select a product first.");
            return;
        }

        try {
            String name = tfName.getText().trim();
            String cat = tfCategory.getText().trim();
            int qty = Integer.parseInt(tfQty.getText().trim());
            double price = Double.parseDouble(tfPrice.getText().trim());

            int oldQty = sel.getQuantity();
            boolean ok = ProductDAO.update(sel.getId(), name, cat, qty, price);

            if (ok) {
                String newStatus = new Product(sel.getId(), name, cat, qty, price).getStatus();
                HistoryDAO.log("UPDATE", name, "Qty: " + oldQty + " → " + qty + " | Status: " + newStatus);

                // save image if chosen
                if (selectedImagePath != null && !selectedImagePath.isBlank()) {
                    ProductImageDAO.upsertImagePath(sel.getId(), selectedImagePath);
                }
            }

            status.setText(ok ? "Product updated." : "Update failed.");
            refresh();
        } catch (Exception ex) {
            status.setText("Invalid input (qty/price must be numbers).");
        }
    }

    private void removeSelected() {
        Product sel = table.getSelectionModel().getSelectedItem();
        if (sel == null) {
            status.setText("Select a product first.");
            return;
        }

        boolean ok = ProductDAO.delete(sel.getId());
        if (ok) {
            HistoryDAO.log("DELETE", sel.getName(), "Removed product.");
        }

        status.setText(ok ? "Product removed." : "Remove failed.");
        selectedImagePath = null;
        refresh();
    }

    private void chooseImage() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Select Product Image");
        fc.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        File file = fc.showOpenDialog(root.getScene().getWindow());
        if (file == null) return;

        try {
            Path imagesDir = Paths.get("product_images");
            if (!Files.exists(imagesDir)) Files.createDirectories(imagesDir);

            String ext = getFileExtension(file.getName());
            String newName = UUID.randomUUID().toString() + (ext.isEmpty() ? "" : "." + ext);

            Path target = imagesDir.resolve(newName);
            Files.copy(file.toPath(), target, StandardCopyOption.REPLACE_EXISTING);

            selectedImagePath = target.toAbsolutePath().toString();
            status.setText("Image selected. Click 'Edit Selected' to save.");
        } catch (Exception ex) {
            status.setText("Failed to select image.");
            ex.printStackTrace();
        }
    }

    private String getFileExtension(String name) {
        int dot = name.lastIndexOf('.');
        if (dot < 0) return "";
        return name.substring(dot + 1);
    }

    public Parent getView() {
        return root;
    }
}