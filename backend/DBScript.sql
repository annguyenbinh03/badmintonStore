-- Tạo database
CREATE DATABASE VotProShop;
GO

USE VotProShop;
GO

-- Bảng người dùng
CREATE TABLE Users (
    UserId INT IDENTITY(1,1) PRIMARY KEY,
    Email NVARCHAR(255) UNIQUE NOT NULL,
    [Password] NVARCHAR(255),
    FullName NVARCHAR(100),
	[Role] NVARCHAR(20),
    IsGoogleLogin BIT DEFAULT 0,
    CreatedAt DATETIME DEFAULT GETDATE()
);

-- Bảng thương hiệu
CREATE TABLE Brand (
    BrandId INT IDENTITY(1,1) PRIMARY KEY,
    Name NVARCHAR(100) NOT NULL
);

-- Bảng vợt
CREATE TABLE Racket (
    RacketId INT IDENTITY(1,1) PRIMARY KEY,
    Name NVARCHAR(200) NOT NULL,
    Description NVARCHAR(MAX),
    Price DECIMAL(18,2) NOT NULL,
    BrandId INT NOT NULL,
    Weight NVARCHAR(10),
    Balance NVARCHAR(20),
    Tension NVARCHAR(20),
    Active BIT DEFAULT 1,
	ImageUrl NVARCHAR(500),
	Quantity INT DEFAULT 0,
    CreatedAt DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (BrandId) REFERENCES Brand(BrandId)
);

-- Bảng đơn hàng
CREATE TABLE Orders (
    OrderId INT IDENTITY(1,1) PRIMARY KEY,
    UserId INT NOT NULL,
    TotalAmount DECIMAL(18,2),
    ShippingAddress NVARCHAR(255),
    PaymentMethod NVARCHAR(50),
    Status NVARCHAR(50) DEFAULT 'Đang xử lý',
    CreatedAt DATETIME DEFAULT GETDATE(),

    FOREIGN KEY (UserId) REFERENCES Users(UserId)
);

-- Bảng chi tiết đơn hàng
CREATE TABLE OrderDetail (
    OrderDetailId INT IDENTITY(1,1) PRIMARY KEY,
    OrderId INT NOT NULL,
    RacketId INT NOT NULL,
    Quantity INT NOT NULL CHECK (Quantity > 0),
    UnitPrice DECIMAL(18,2) NOT NULL,

    FOREIGN KEY (OrderId) REFERENCES Orders(OrderId),
    FOREIGN KEY (RacketId) REFERENCES Racket(RacketId)
);


-- Người dùng
INSERT INTO Users (Email, [Password], FullName, [Role], IsGoogleLogin)
VALUES 
('user@gmail.com', 'user', N'Nguyễn Văn A', 'User', 0),
('manager@gmail.com', 'manager', N'Trần Thị B', 'Manager', 0),
('manager2@shop.com', 'manager', N'Quản Lý Shop', 'Manager', 0),
('user2@example.com', 'user', N'Lê Thị C', 'User', 0);


INSERT INTO Brand (Name) VALUES 
('Yonex'), 
('Lining'), 
('Victor');

-- Vợt (15 cây)
INSERT INTO Racket (Name, Description, Price, BrandId, Weight, Balance, Tension, Active, Quantity, ImageUrl)
VALUES
(N'Yonex Astrox 99 Pro', N'Vợt tấn công mạnh mẽ.', 3990000, 1, '3U', 'head-heavy', '24-28 lbs', 1, 10, 'https://cdn.shopvnb.com/uploads/gallery/vot-cau-long-yonex-astrox-99-pro-do-chinh-hang_1735064653.jpg'),
(N'Lining Turbo Charging 75', N'Cân bằng toàn diện.', 2790000, 2, '4U', 'even', '22-27 lbs', 1, 15, 'https://cdn.shopvnb.com/uploads/san_pham/vot-cau-long-lining-turbo-charging-75i-chinh-hang-1.webp'),
(N'Victor Brave Sword 12', N'Phản công nhanh.', 3190000, 1, '4U', 'head-light', '22-26 lbs', 1, 8, 'https://cdn.shopvnb.com/uploads/gallery/vot-cau-long-victor-brave-sword-12-pro-chinh-hang_1737312826.webp'),
(N'Yonex Nanoflare 700', N'Vợt nhẹ cho nữ.', 2590000, 1, '5U', 'head-light', '20-26 lbs', 1, 12, 'https://cdn.shopvnb.com/uploads/gallery/vot-cau-long-yonex-nanoflare-700-pro-chinh-hang_1727042472.webp'),
(N'Lining Windstorm 72', N'Nhẹ và linh hoạt.', 2390000, 2, '5U', 'even', '22-26 lbs', 1, 5, 'https://cdn.shopvnb.com/uploads/gallery/vot-cau-long-lining-windstorm-72-speed-chinh-hang_1727048867.jpg'),
(N'Victor Jetspeed S12', N'Tốc độ cực nhanh.', 3590000, 3, '4U', 'even', '24-28 lbs', 1, 7, 'https://cdn.shopvnb.com/uploads/san_pham/vot-cau-long-victor-jetspeed-s12-m-chinh-hang-1.webp'),
(N'Yonex Duora 10', N'Đa năng công/thủ.', 3290000, 1, '3U', 'even', '24-28 lbs', 1, 9, 'https://cdn.shopvnb.com/uploads/gallery/vot-cau-long-the-3rd-game-duora-100z-trang_1703207731.webp'),
(N'Lining 3D Calibar 900', N'Tấn công mạnh mẽ.', 3890000, 2, '3U', 'head-heavy', '26-30 lbs', 1, 6, 'https://cdn.shopvnb.com/uploads/san_pham/vot-cau-long-lining-calibar-900-chinh-hang-1.webp'),
(N'Yonex Arcsaber 11 Pro', N'Độ chính xác cao.', 3690000, 1, '3U', 'even', '24-28 lbs', 1, 4, 'https://cdn.shopvnb.com/uploads/san_pham/vot-cau-long-yonex-arcsaber-11-pro-chinh-hang-1.webp'),
(N'Lining Tectonic 7', N'Kiểm soát tốt.', 2790000, 2, '4U', 'even', '22-26 lbs', 1, 11, 'https://cdn.shopvnb.com/uploads/san_pham/vot-cau-long-lining-tectonic-7d-chinh-hang-1.webp'),
(N'Victor DriveX 9X', N'Trung hòa tấn công/thủ.', 3490000, 3, '4U', 'even', '24-28 lbs', 1, 5, 'https://cdn.shopvnb.com/uploads/san_pham/vot-cau-long-victor-drivex-9x-chinh-hang-4.webp'),
(N'Yonex Muscle Power 33 Light', N'Công phá cực mạnh.', 1380000, 1, '3U', 'head-heavy', '26-30 lbs', 1, 6, 'https://cdn.shopvnb.com/uploads/san_pham/vot-cau-long-yonex-muscle-power-33-light-chinh-hang-3.webp'),
(N'Lining Woods N90 III', N'Công thủ toàn diện.', 4940000, 2, '5U', 'even', '20-25 lbs', 1, 20, 'https://cdn.shopvnb.com/uploads/san_pham/vot-cau-long-lining-woods-n90-iii-1.webp'),
(N'Victor AuraSpeed 90K', N'Tốc độ xử lý tốt.', 3590000, 3, '4U', 'head-light', '22-26 lbs', 1, 6, 'https://cdn.shopvnb.com/uploads/gallery/combo-mua-vot-cau-long-victor-ars-90k-ii-td-tang-vot-victor-ars-9990k-vot-victor-tk220h-ii_1744309071.webp');

-- Đơn hàng
INSERT INTO Orders (UserId, TotalAmount, ShippingAddress, PaymentMethod, Status)
VALUES
(1, 7180000, N'123 Đường Cầu Lông, TP.HCM', 'COD', 'Đang giao'),
(2, 2590000, N'456 Trần Hưng Đạo, Hà Nội', 'Chuyển khoản', 'Đang xử lý'),
(4, 7780000, N'789 Lê Văn Sỹ, Đà Nẵng', 'COD', 'Đã nhận');

-- Chi tiết đơn hàng
INSERT INTO OrderDetail (OrderId, RacketId, Quantity, UnitPrice)
VALUES
(1, 1, 1, 3990000),
(1, 2, 1, 2790000),
(2, 4, 1, 2590000),
(3, 9, 2, 3890000);

