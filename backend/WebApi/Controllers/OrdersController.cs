using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Repository.Models;
using Service.Interfaces;
using Service.Requests;

namespace WebApi.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class OrdersController : ControllerBase
    {
        private readonly IOrderService _orderService;

        public OrdersController(IOrderService orderService)
        {
            _orderService = orderService;
        }

        [HttpGet]
        public async Task<IActionResult> GetAll()
        {
            var orders = await _orderService.GetAllAsync();
            return Ok(orders);
        }

        [HttpGet("{id}")]
        public async Task<IActionResult> GetById(int id)
        {
            var order = await _orderService.GetByIdAsync(id);
            return Ok(order);
        }

        [HttpPost]
        public async Task<IActionResult> Create(OrderCreationRequest request)
        {
            var response = await _orderService.AddAsync(request);
            return Ok(response);
        }

        //[HttpPut("{id}")]
        //public async Task<IActionResult> Update(int id, Order order)
        //{
        //    if (id != order.OrderId) return BadRequest();
        //    await _orderService.UpdateAsync(order);
        //    return NoContent();
        //}

        //[HttpDelete("{id}")]
        //public async Task<IActionResult> Delete(int id)
        //{
        //    await _orderService.DeleteAsync(id);
        //    return NoContent();
        //}
    }
}
