using Repository.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Repository.Interfaces
{
    public interface IRacketRepository : IRepository<Racket>
    {
        Task<IEnumerable<Racket>> GetAllWithBrandAsync();

        Task<Racket?> GetByIdWithBrandAsync(int id);
    }
}
