﻿// <auto-generated> This file has been auto generated by EF Core Power Tools. </auto-generated>
#nullable disable
using System;
using System.Collections.Generic;

namespace Repository.Models;

public partial class Brand
{
    public int BrandId { get; set; }

    public string Name { get; set; }

    public virtual ICollection<Racket> Rackets { get; set; } = new List<Racket>();
}