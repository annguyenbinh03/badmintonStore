using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Repository.Models;
using Service.Interfaces;

namespace WebApi.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class RacketsController : ControllerBase
    {
        private readonly IRacketService _racketService;

        public RacketsController(IRacketService racketService)
        {
            _racketService = racketService;
        }

        [HttpGet]
        public async Task<IActionResult> GetAll()
        {
            var rackets = await _racketService.GetAllAsync();
            return Ok(rackets);
        }

        [HttpGet("{id}")]
        public async Task<IActionResult> GetById(int id)
        {
            var racket = await _racketService.GetByIdAsync(id);
            return racket == null ? NotFound() : Ok(racket);
        }

        [HttpPost]
        public async Task<IActionResult> Create(Racket racket)
        {
            await _racketService.AddAsync(racket);
            return CreatedAtAction(nameof(GetById), new { id = racket.RacketId }, racket);
        }

        [HttpPut("{id}")]
        public async Task<IActionResult> Update(int id, Racket racket)
        {
            if (id != racket.RacketId) return BadRequest();
            await _racketService.UpdateAsync(racket);
            return NoContent();
        }

        [HttpDelete("{id}")]
        public async Task<IActionResult> Delete(int id)
        {
            await _racketService.DeleteAsync(id);
            return NoContent();
        }
    }
}
