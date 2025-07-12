using Repository.Interfaces;
using Repository.Models;
using Service.Interfaces;
using Service.Responses;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Service.Implementations
{
    public class RacketService : IRacketService
    {
        private readonly IRacketRepository _racketRepository;

        public RacketService(IRacketRepository racketRepository)
        {
            _racketRepository = racketRepository;
        }

        public async Task<ApiResponse<IEnumerable<RacketGetAllResponse>>> GetAllAsync()
        {
            var rackets = await _racketRepository.GetAllAsync(); 

            var responses = rackets.Select(racket => new RacketGetAllResponse
            {
                RacketId = racket.RacketId,
                Name = racket.Name,
                Description = racket.Description ?? string.Empty,
                Price = racket.Price,
                BrandId = racket.BrandId,
                Weight = racket.Weight ?? string.Empty,
                Balance = racket.Balance ?? string.Empty,
                Tension = racket.Tension ?? string.Empty,
                Active = racket.Active ?? true,
                ImageUrl = racket.ImageUrl ?? string.Empty,
                Quantity = racket.Quantity ?? 0,
                CreatedAt = racket.CreatedAt ?? DateTime.MinValue,
                BrandName = racket.Brand?.Name ?? string.Empty
            }).ToList();

            return new ApiResponse<IEnumerable<RacketGetAllResponse>>
            {
                Success = true,
                Data = responses
            };
        }


        public async Task<ApiResponse<RacketGetAllResponse>> GetByIdAsync(int id)
        {
            var racket = await _racketRepository.GetByIdWithBrandAsync(id);

            if (racket == null)
            {
                return new ApiResponse<RacketGetAllResponse>
                {
                    Success = false,
                    Message = $"Không tìm thấy vợt với ID = {id}"
                };
            }

            var response = new RacketGetAllResponse
            {
                RacketId = racket.RacketId,
                Name = racket.Name,
                Description = racket.Description ?? string.Empty,
                Price = racket.Price,
                BrandId = racket.BrandId,
                Weight = racket.Weight ?? string.Empty,
                Balance = racket.Balance ?? string.Empty,
                Tension = racket.Tension ?? string.Empty,
                Active = racket.Active ?? true,
                ImageUrl = racket.ImageUrl ?? string.Empty,
                Quantity = racket.Quantity ?? 0,
                CreatedAt = racket.CreatedAt ?? DateTime.MinValue,
                BrandName = racket.Brand?.Name ?? string.Empty
            };

            return new ApiResponse<RacketGetAllResponse>
            {
                Success = true,
                Data = response
            };
        }

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
