using Repository.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Service.Interfaces
{
    public interface IRacketService
    {
        Task<IEnumerable<Racket>> GetAllAsync();
        Task<Racket?> GetByIdAsync(int id);
        Task AddAsync(Racket racket);
        Task UpdateAsync(Racket racket);
        Task DeleteAsync(int id);
    }
}
