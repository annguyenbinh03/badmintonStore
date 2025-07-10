using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Service.Responses
{
    public class OrderGetAllResponse
    {
        public int OrderId { get; set; }
        public string ShippingAddress { get; set; } = string.Empty;
        public string PaymentMethod { get; set; } = string.Empty;
        public string Status { get; set; } = string.Empty;
        public decimal TotalAmount { get; set; }
        public DateTime CreatedAt { get; set; }

        public UserInfo User { get; set; }
        public List<OrderDetailInfo> Items { get; set; } = new();
    }
}

public class UserInfo
{
    public int UserId { get; set; }
    public string FullName { get; set; } = string.Empty;
    public string Email { get; set; } = string.Empty;
}
public class OrderDetailInfo
{
    public int RacketId { get; set; }
    public string RacketName { get; set; } = string.Empty;
    public int Quantity { get; set; }
    public decimal UnitPrice { get; set; }
    public string ImageUrl { get; set; } = string.Empty;
}
