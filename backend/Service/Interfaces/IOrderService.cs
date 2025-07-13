using Repository.Models;
using Service.Requests;
using Service.Responses;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Service.Interfaces
{
    public interface IOrderService
    {
        Task<ApiResponse<IEnumerable<OrderGetAllResponse>>> GetAllAsync();
        Task<ApiResponse<OrderGetAllResponse>> GetByIdAsync(int id);
        Task<ApiResponse<int>> AddAsync(OrderCreationRequest order);
        Task<ApiResponse<IEnumerable<OrderGetAllResponse>>> GetByUserIdAsync(int userId);
        Task UpdateAsync(Order order);
        Task DeleteAsync(int id);
    }
}
