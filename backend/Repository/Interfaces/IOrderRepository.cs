using Repository.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Repository.Interfaces
{
    public interface IOrderRepository : IRepository<Order>
    {
        new Task<IEnumerable<Order>> GetAllAsync();
        Task<Order?> GetByIdWithDetailAsync(int id);
    }
}
