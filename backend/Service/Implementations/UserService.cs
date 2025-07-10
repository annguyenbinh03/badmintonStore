
using Repository.Interfaces;
using Repository.Models;
using Service.Interfaces;
using Service.Requests;
using Service.Responses;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Service.Implementations
{
    public class UserService : IUserService
    {
        private readonly IUserRepository _userRepository;

        public UserService(IUserRepository userRepository)
        {
            _userRepository = userRepository;
        }

        public async Task<User?> GetByIdAsync(int id) => await _userRepository.GetByIdAsync(id);

        public async Task<User?> GetByEmailAsync(string email) => await _userRepository.GetByEmailAsync(email);

        public async Task<IEnumerable<User>> GetAllAsync() => await _userRepository.GetAllAsync();

        public async Task AddAsync(User user)
        {
            await _userRepository.AddAsync(user);
            await _userRepository.SaveChangesAsync();
        }

        public async Task UpdateAsync(User user)
        {
            _userRepository.Update(user);
            await _userRepository.SaveChangesAsync();
        }

        public async Task DeleteAsync(int id)
        {
            var user = await _userRepository.GetByIdAsync(id);
            if (user != null)
            {
                _userRepository.Delete(user);
                await _userRepository.SaveChangesAsync();
            }
        }

        public async Task<ApiResponse<User>> Login(LoginRequest request)
        {
            User? user = await _userRepository.GetByEmailAndPasswordAsync(request.Email, request.Password);

            if (user == null) {
                return new ApiResponse<User>
                {
                    Success = false,
                    Message = "Sai tài khoản hoặc mật khẩu"
                };
            }
            else
            {
                return new ApiResponse<User>
                {
                    Success = true,
                    Data = user
                };
            }
        }
    }
}
