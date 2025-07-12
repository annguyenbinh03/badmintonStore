using Repository.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Service.Responses
{
    public class RacketGetAllResponse
    {
        public int RacketId { get; set; }

        public string Name { get; set; } = string.Empty;

        public string Description { get; set; } = string.Empty;

        public decimal Price { get; set; }

        public int BrandId { get; set; }

        public string Weight { get; set; } = string.Empty;

        public string Balance { get; set; } = string.Empty;

        public string Tension { get; set; } = string.Empty;

        public bool? Active { get; set; }

        public string ImageUrl { get; set; } = string.Empty;

        public int? Quantity { get; set; }

        public DateTime? CreatedAt { get; set; }

        public string BrandName { get; set; } = string.Empty;

    }
}
