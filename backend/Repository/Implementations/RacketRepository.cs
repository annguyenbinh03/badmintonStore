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
    public class RacketRepository : Repository<Racket>, IRacketRepository
    {
        private readonly DbContext _context;

        public RacketRepository(VotProShopContext context) : base(context)
        {
            _context = context;
        }

        public async Task<IEnumerable<Racket>> GetAllWithBrandAsync()
        {
            return await _context.Set<Racket>()
                 .Include(r => r.Brand)
                 .ToListAsync();
        }

        public async Task<Racket?> GetByIdWithBrandAsync(int id)
        {
            return await _context.Set<Racket>()
                .Include(r => r.Brand)
                .FirstOrDefaultAsync(r => r.RacketId == id);
        }

    }
}
