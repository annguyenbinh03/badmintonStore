using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Service.Requests
{
    public class OrderCreationRequest
    {
        public int UserId { get; set; }

        public string ShippingAddress { get; set; } = string.Empty;

        public string PaymentMethod { get; set; } = "COD";

        public List<OrderItemRequest> Items { get; set; } = new();
    }

    public class OrderItemRequest
    {
        public int RacketId { get; set; }
        public int Quantity { get; set; }
    }
}
