using System;
using System.Collections.Generic;
using Microsoft.EntityFrameworkCore;
using Repository.Models;

namespace Repository.DbContexts;

public partial class VotProShopContext : DbContext
{
    public VotProShopContext(DbContextOptions<VotProShopContext> options)
        : base(options)
    {
    }

    public virtual DbSet<Brand> Brands { get; set; }

    public virtual DbSet<Order> Orders { get; set; }

    public virtual DbSet<OrderDetail> OrderDetails { get; set; }

    public virtual DbSet<Racket> Rackets { get; set; }  

    public virtual DbSet<User> Users { get; set; }

    protected override void OnModelCreating(ModelBuilder modelBuilder)
    {
        modelBuilder.Entity<Brand>(entity =>
        {
            entity.HasKey(e => e.BrandId).HasName("PK__Brand__DAD4F05E326455AF");

            entity.ToTable("Brand");

            entity.Property(e => e.Name)
                .IsRequired()
                .HasMaxLength(100);
        });

        modelBuilder.Entity<Order>(entity =>
        {
            entity.HasKey(e => e.OrderId).HasName("PK__Orders__C3905BCFAE274CDA");

            entity.Property(e => e.CreatedAt)
                .HasDefaultValueSql("(getdate())")
                .HasColumnType("datetime");
            entity.Property(e => e.PaymentMethod).HasMaxLength(50);
            entity.Property(e => e.ShippingAddress).HasMaxLength(255);
            entity.Property(e => e.Status)
                .HasMaxLength(50)
                .HasDefaultValue("Ðang x? lý");
            entity.Property(e => e.TotalAmount).HasColumnType("decimal(18, 2)");

            entity.HasOne(d => d.User).WithMany(p => p.Orders)
                .HasForeignKey(d => d.UserId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK__Orders__UserId__45F365D3");
        });

        modelBuilder.Entity<OrderDetail>(entity =>
        {
            entity.HasKey(e => e.OrderDetailId).HasName("PK__OrderDet__D3B9D36CACFD6E40");

            entity.ToTable("OrderDetail");

            entity.Property(e => e.UnitPrice).HasColumnType("decimal(18, 2)");

            entity.HasOne(d => d.Order).WithMany(p => p.OrderDetails)
                .HasForeignKey(d => d.OrderId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK__OrderDeta__Order__49C3F6B7");

            entity.HasOne(d => d.Racket).WithMany(p => p.OrderDetails)
                .HasForeignKey(d => d.RacketId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK__OrderDeta__Racke__4AB81AF0");
        });

        modelBuilder.Entity<Racket>(entity =>
        {
            entity.HasKey(e => e.RacketId).HasName("PK__Racket__FA60C5E854785133");

            entity.ToTable("Racket");

            entity.Property(e => e.Active).HasDefaultValue(true);
            entity.Property(e => e.Balance).HasMaxLength(20);
            entity.Property(e => e.CreatedAt)
                .HasDefaultValueSql("(getdate())")
                .HasColumnType("datetime");
            entity.Property(e => e.ImageUrl).HasMaxLength(500);
            entity.Property(e => e.Name)
                .IsRequired()
                .HasMaxLength(200);
            entity.Property(e => e.Price).HasColumnType("decimal(18, 2)");
            entity.Property(e => e.Quantity).HasDefaultValue(0);
            entity.Property(e => e.Tension).HasMaxLength(20);
            entity.Property(e => e.Weight).HasMaxLength(10);

            entity.HasOne(d => d.Brand).WithMany(p => p.Rackets)
                .HasForeignKey(d => d.BrandId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK__Racket__BrandId__412EB0B6");
        });

        modelBuilder.Entity<User>(entity =>
        {
            entity.HasKey(e => e.UserId).HasName("PK__Users__1788CC4CC5052760");

            entity.HasIndex(e => e.Email, "UQ__Users__A9D10534384A5257").IsUnique();

            entity.Property(e => e.CreatedAt)
                .HasDefaultValueSql("(getdate())")
                .HasColumnType("datetime");
            entity.Property(e => e.Email)
                .IsRequired()
                .HasMaxLength(255);
            entity.Property(e => e.FullName).HasMaxLength(100);
            entity.Property(e => e.IsGoogleLogin).HasDefaultValue(false);
            entity.Property(e => e.Password).HasMaxLength(255);
            entity.Property(e => e.Role).HasMaxLength(20);
        });

        OnModelCreatingPartial(modelBuilder);
    }

    partial void OnModelCreatingPartial(ModelBuilder modelBuilder);
}