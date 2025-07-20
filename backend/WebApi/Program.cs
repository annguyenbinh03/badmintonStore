
using Microsoft.EntityFrameworkCore;
using Repository.DbContexts;
using Repository.Implementations;
using Repository.Interfaces;
using Repository.Models;
using Service.Implementations;
using Service.Interfaces;

namespace WebApi
{
    public class Program
    {
        public static void Main(string[] args)
        {
            var builder = WebApplication.CreateBuilder(args);

            // Add DbContext
            builder.Services.AddDbContext<VotProShopContext>(options =>
                 options.UseSqlServer(builder.Configuration.GetConnectionString("DefaultConnection")));

            // Repository DI
            builder.Services.AddScoped<IUserRepository, UserRepository>();
            builder.Services.AddScoped<IOrderRepository, OrderRepository>();
            builder.Services.AddScoped<IRacketRepository, RacketRepository>();
            builder.Services.AddScoped<IRepository<OrderDetail>, Repository<OrderDetail>>();
            builder.Services.AddScoped<IRepository<Racket>, Repository<Racket>>();

            // Service DI
            builder.Services.AddScoped<IUserService, UserService>();
            builder.Services.AddScoped<IRacketService, RacketService>();
            builder.Services.AddScoped<IOrderService, OrderService>();

            builder.Services.AddControllers();
            builder.Services.AddRouting(options => options.LowercaseUrls = true);
            // Learn more about configuring Swagger/OpenAPI at https://aka.ms/aspnetcore/swashbuckle
            builder.Services.AddEndpointsApiExplorer();
            builder.Services.AddSwaggerGen();

            var app = builder.Build();

            // Configure the HTTP request pipeline.
            if (app.Environment.IsDevelopment())
            {
                app.UseSwagger();
                app.UseSwaggerUI();
            }

            app.UseHttpsRedirection();

            app.UseAuthorization();


            app.MapControllers();

            app.Run();
        }
    }
}