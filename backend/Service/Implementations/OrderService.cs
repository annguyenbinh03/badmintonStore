using Azure.Core;
using Repository.Implementations;
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
    public class OrderService : IOrderService
    {
        private readonly IOrderRepository _orderRepository;
        private readonly IRepository<OrderDetail> _orderDetailRepository;
        private readonly IRepository<Racket> _racketRepository;
        private readonly IUserRepository _userRepository;

        public OrderService(IOrderRepository orderRepository, IRepository<OrderDetail> orderDetailRepository, IRepository<Racket> racketRepository, IUserRepository userRepository)
        {
            _orderRepository = orderRepository;
            _orderDetailRepository = orderDetailRepository;
            _racketRepository = racketRepository;
            _userRepository = userRepository;
        }

        public async Task<ApiResponse<IEnumerable<OrderGetAllResponse>>> GetAllAsync()
        {
            var orders = await _orderRepository.GetAllAsync();

            var result = orders.Select(order => new OrderGetAllResponse
            {
                OrderId = order.OrderId,
                ShippingAddress = order.ShippingAddress,
                PaymentMethod = order.PaymentMethod,
                Status = order.Status,
                TotalAmount = order.TotalAmount ?? 0m,
                CreatedAt = order.CreatedAt ?? DateTime.MinValue,

                User = new UserInfo
                {
                    UserId = order.User.UserId,
                    FullName = order.User.FullName,
                    Email = order.User.Email
                },

                Items = order.OrderDetails.Select(od => new OrderDetailInfo
                {
                    RacketId = od.RacketId,
                    RacketName = od.Racket?.Name ?? "",
                    Quantity = od.Quantity,
                    UnitPrice = od.UnitPrice,
                    ImageUrl = od.Racket?.ImageUrl ?? ""
                }).ToList()
            });

            var response = new ApiResponse<IEnumerable<OrderGetAllResponse>>
            {
                Success = true,
                Data = result
            };

            return response;
        }


        public async Task<ApiResponse<OrderGetAllResponse>> GetByIdAsync(int id)
        {
            var order = await _orderRepository.GetByIdWithDetailAsync(id);

            if (order == null)
            {
                return new ApiResponse<OrderGetAllResponse>
                {
                    Success = false,
                    Message = $"Không tìm thấy đơn hàng với ID = {id}"
                };
            }

            var response = new OrderGetAllResponse
            {
                OrderId = order.OrderId,
                ShippingAddress = order.ShippingAddress,
                PaymentMethod = order.PaymentMethod,
                Status = order.Status,
                TotalAmount = order.TotalAmount ?? 0m,
                CreatedAt = order.CreatedAt ?? DateTime.MinValue,
                User = new UserInfo
                {
                    UserId = order.User.UserId,
                    FullName = order.User.FullName,
                    Email = order.User.Email
                },
                Items = order.OrderDetails.Select(od => new OrderDetailInfo
                {
                    RacketId = od.RacketId,
                    RacketName = od.Racket?.Name ?? "",
                    Quantity = od.Quantity,
                    UnitPrice = od.UnitPrice,
                    ImageUrl = od.Racket?.ImageUrl ?? ""
                }).ToList()
            };

            return new ApiResponse<OrderGetAllResponse>
            {
                Success = true,
                Data = response
            };
        }

        public async Task<ApiResponse<int>> AddAsync(OrderCreationRequest request)
        {
            // Kiểm tra danh sách rỗng
            if (request.Items == null || !request.Items.Any())
            {
                return new ApiResponse<int>
                {
                    Success = false,
                    Message = "Đơn hàng phải có ít nhất 1 sản phẩm."
                };
            }

            var racketIds = request.Items.Select(i => i.RacketId).ToList();
            var racketsInDb = (await _racketRepository.GetAllAsync())
                .Where(r => racketIds.Contains(r.RacketId))
                .ToDictionary(r => r.RacketId, r => r);

            // Kiểm tra racket hợp lệ
            var invalidIds = racketIds.Where(id => !racketsInDb.ContainsKey(id)).ToList();
            if (invalidIds.Any())
            {
                return new ApiResponse<int>
                {
                    Success = false,
                    Message = $"RacketId không hợp lệ: {string.Join(", ", invalidIds)}"
                };
            }
            // Tính tổng
            decimal total = 0;
            foreach (var item in request.Items)
            {
                var racket = racketsInDb[item.RacketId];
                total += racket.Price * item.Quantity;
            }

            var user = await _userRepository.GetByIdAsync(request.UserId);
            if (user == null)
            {
                return new ApiResponse<int>
                {
                    Success = false,
                    Message = $"Người dùng với ID {request.UserId} không tồn tại."
                };
            }

            // Tạo order
            var newOrder = new Order
            {
                UserId = request.UserId,
                TotalAmount = total,
                ShippingAddress = request.ShippingAddress,
                PaymentMethod = request.PaymentMethod,
                Status = "Đang xử lý",
                CreatedAt = DateTime.Now
            };
            await _orderRepository.AddAsync(newOrder);
            await _orderRepository.SaveChangesAsync();

            // Tạo OrderDetail
            foreach (var item in request.Items)
            {
                var price = racketsInDb[item.RacketId].Price;
                var detail = new OrderDetail
                {
                    OrderId = newOrder.OrderId,
                    RacketId = item.RacketId,
                    Quantity = item.Quantity,
                    UnitPrice = price
                };
                await _orderDetailRepository.AddAsync(detail);
            }
            await _orderDetailRepository.SaveChangesAsync();

            return new ApiResponse<int>
            {
                Success = true,
                Message = "Tạo order thành công với Id:" + newOrder.OrderId,
                Data = newOrder.OrderId
            };
        }

        public async Task<ApiResponse<IEnumerable<OrderGetAllResponse>>> GetByUserIdAsync(int userId)
        {
            var orders = await _orderRepository.GetOrderHistory(userId);

            if (orders == null || !orders.Any())
            {
                return new ApiResponse<IEnumerable<OrderGetAllResponse>>
                {
                    Success = false,
                    Message = $"Không tìm thấy đơn hàng nào cho userId = {userId}"
                };
            }

            var result = orders.Select(order => new OrderGetAllResponse
            {
                OrderId = order.OrderId,
                ShippingAddress = order.ShippingAddress,
                PaymentMethod = order.PaymentMethod,
                Status = order.Status,
                TotalAmount = order.TotalAmount ?? 0m,
                CreatedAt = order.CreatedAt ?? DateTime.MinValue,

                User = new UserInfo
                {
                    UserId = order.User.UserId,
                    FullName = order.User.FullName,
                    Email = order.User.Email
                },

                Items = order.OrderDetails.Select(od => new OrderDetailInfo
                {
                    RacketId = od.RacketId,
                    RacketName = od.Racket?.Name ?? "",
                    Quantity = od.Quantity,
                    UnitPrice = od.UnitPrice,
                    ImageUrl = od.Racket?.ImageUrl ?? ""
                }).ToList()
            });

            var response = new ApiResponse<IEnumerable<OrderGetAllResponse>>
            {
                Success = true,
                Data = result
            };

            return response;
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
