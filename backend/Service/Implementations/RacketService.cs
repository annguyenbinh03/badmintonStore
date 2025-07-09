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
    public class RacketService : IRacketService
    {
        private readonly IRepository<Racket> _racketRepository;

        public RacketService(IRepository<Racket> racketRepository)
        {
            _racketRepository = racketRepository;
        }

        public async Task<IEnumerable<Racket>> GetAllAsync() => await _racketRepository.GetAllAsync();

        public async Task<Racket?> GetByIdAsync(int id) => await _racketRepository.GetByIdAsync(id);

        public async Task AddAsync(Racket racket)
        {
            await _racketRepository.AddAsync(racket);
            await _racketRepository.SaveChangesAsync();
        }

        public async Task UpdateAsync(Racket racket)
        {
            _racketRepository.Update(racket);
            await _racketRepository.SaveChangesAsync();
        }

        public async Task DeleteAsync(int id)
        {
            var item = await _racketRepository.GetByIdAsync(id);
            if (item != null)
            {
                _racketRepository.Delete(item);
                await _racketRepository.SaveChangesAsync();
            }
        }
    }
}
