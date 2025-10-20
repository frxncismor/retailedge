insert into retailedge.products (sku, name, description, price, stock)
values
  ('SKU-001','Trail Running Shoes','Lightweight trail shoes', 119.99, 50),
  ('SKU-002','Compression Tee','Breathable training tee', 29.90, 200),
  ('SKU-003','Hydration Pack','2L hydration backpack', 54.50, 80)
on conflict (sku) do nothing;
