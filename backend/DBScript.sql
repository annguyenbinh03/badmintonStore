-- Tạo database
CREATE DATABASE VotProShop;
GO

USE VotProShop;
GO

-- Bảng người dùng
CREATE TABLE Users (
    UserId INT IDENTITY(1,1) PRIMARY KEY,
    Email NVARCHAR(255) UNIQUE NOT NULL,
    PasswordHash NVARCHAR(255),
    FullName NVARCHAR(100),
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

-- Bảng đánh giá vợt
CREATE TABLE RacketReview (
    ReviewId INT IDENTITY(1,1) PRIMARY KEY,
    RacketId INT NOT NULL,
    UserId INT NOT NULL,
    Rating INT CHECK (Rating BETWEEN 1 AND 5),
    Comment NVARCHAR(MAX),
    CreatedAt DATETIME DEFAULT GETDATE(),

    FOREIGN KEY (RacketId) REFERENCES Racket(RacketId),
    FOREIGN KEY (UserId) REFERENCES Users(UserId)
);

-- Thêm dữ liệu mẫu

-- Người dùng
INSERT INTO Users (Email, PasswordHash, FullName, IsGoogleLogin)
VALUES 
('a@example.com', 'hashed_password_1', N'Nguyễn Văn A', 0),
('b@example.com', NULL, N'Trần Thị B', 1);

-- Thương hiệu
INSERT INTO Brand (Name) VALUES 
('Yonex'), 
('Lining'), 
('Victor');

-- Vợt
INSERT INTO Racket (Name, Description, Price, BrandId, Weight, Balance, Tension, Active)
VALUES
(N'Yonex Astrox 99 Pro', N'Vợt tấn công mạnh mẽ với công nghệ tiên tiến.', 3990000, 1, '3U', 'head-heavy', '24-28 lbs', 1),
(N'Lining Turbo Charging 75', N'Vợt cân bằng, linh hoạt phù hợp mọi phong cách.', 2790000, 2, '4U', 'even', '22-27 lbs', 1),
(N'Victor Brave Sword 12', N'Vợt nhẹ, nhanh, phản công cực tốt.', 3190000, 3, '4U', 'head-light', '22-26 lbs', 1);

-- Đơn hàng
INSERT INTO Orders (UserId, TotalAmount, ShippingAddress, PaymentMethod, Status)
VALUES 
(1, 10370000, N'123 Đường Cầu Lông, TP.HCM', N'COD', N'Đang giao');

-- Chi tiết đơn hàng
INSERT INTO OrderDetail (OrderId, RacketId, Quantity, UnitPrice)
VALUES 
(1, 1, 1, 3990000),
(1, 3, 2, 3190000);

-- Đánh giá
INSERT INTO RacketReview (RacketId, UserId, Rating, Comment)
VALUES
(1, 1, 5, N'Vợt đánh cực đã, smash rất mạnh.'),
(2, 2, 4, N'Cảm giác đánh tốt, nhẹ tay và dễ điều khiển.');