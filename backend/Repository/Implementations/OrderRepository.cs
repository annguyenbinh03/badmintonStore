using Microsoft.EntityFrameworkCore;
using Repository.DbContexts;
using Repository.Interfaces;
using Repository.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Repository.Implementations
{
    public class OrderRepository : Repository<Order>, IOrderRepository
    {
        private readonly DbContext _context;

        public OrderRepository(VotProShopContext context) : base(context)
        {
            _context = context;
        }

        // Ghi đè lại GetAllAsync để include dữ liệu
        public new async Task<IEnumerable<Order>> GetAllAsync()
        {
            return await _context.Set<Order>()
                .Include(o => o.User)
                .Include(o => o.OrderDetails)
                    .ThenInclude(od => od.Racket)
                .ToListAsync();
        }

        // Các method còn lại dùng từ base IRepository
        public async Task<Order> GetByIdAsync(int id)
        {
            return await _context.Set<Order>().FindAsync(id);
        }

        public async Task<Order?> GetByIdWithDetailAsync(int id)
        {
            return await _context.Set<Order>()
                .Include(o => o.User)
                .Include(o => o.OrderDetails)
                    .ThenInclude(od => od.Racket)
                .FirstOrDefaultAsync(o => o.OrderId == id);
        }

        public async Task<IEnumerable<Order>> GetOrderHistory(int userId)
        {
            return await _context.Set<Order>()
                .Include(o => o.User)
                .Include(o => o.OrderDetails)
                    .ThenInclude(od => od.Racket)
                    .Where(o => o.UserId == userId)
                .ToListAsync();
        }
    }
}
