using Repository.Models;
using Service.Responses;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Service.Interfaces
{
    public interface IRacketService
    {
        Task<ApiResponse<IEnumerable<RacketGetAllResponse>>> GetAllAsync();
        Task<ApiResponse<RacketGetAllResponse>> GetByIdAsync(int id);
        Task AddAsync(Racket racket);
        Task UpdateAsync(Racket racket);
        Task DeleteAsync(int id);
    }
}
