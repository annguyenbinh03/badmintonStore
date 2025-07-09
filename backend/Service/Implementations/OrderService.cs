using Repository.Interfaces;
using Repository.Models;
using Service.Interfaces;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Service.Implementations
{
    public class OrderService : IOrderService
    {
        private readonly IRepository<Order> _orderRepository;

        public OrderService(IRepository<Order> orderRepository)
        {
            _orderRepository = orderRepository;
        }

        public async Task<IEnumerable<Order>> GetAllAsync() => await _orderRepository.GetAllAsync();

        public async Task<Order?> GetByIdAsync(int id) => await _orderRepository.GetByIdAsync(id);

        public async Task AddAsync(Order order)
        {
            await _orderRepository.AddAsync(order);
            await _orderRepository.SaveChangesAsync();
        }

        public async Task UpdateAsync(Order order)
        {
            _orderRepository.Update(order);
            await _orderRepository.SaveChangesAsync();
        }

        public async Task DeleteAsync(int id)
        {
            var item = await _orderRepository.GetByIdAsync(id);
            if (item != null)
            {
                _orderRepository.Delete(item);
                await _orderRepository.SaveChangesAsync();
            }
        }
    }
}
