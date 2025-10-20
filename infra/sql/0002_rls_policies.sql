-- Enable RLS
alter table retailedge.products enable row level security;
alter table retailedge.orders enable row level security;
alter table retailedge.order_items enable row level security;

-- Public read-only catalog (no writes)
drop policy if exists "public can read products" on retailedge.products;
create policy "public can read products"
on retailedge.products
for select
to public
using (true);

-- Only authenticated users manage their own orders
-- Assumes Supabase Auth; auth.uid() is set for JWT calls.
drop policy if exists "user can read own orders" on retailedge.orders;
create policy "user can read own orders"
on retailedge.orders
for select
to authenticated
using (customer_id = auth.uid());

drop policy if exists "user can insert own orders" on retailedge.orders;
create policy "user can insert own orders"
on retailedge.orders
for insert
to authenticated
with check (customer_id = auth.uid());

drop policy if exists "user can update own orders" on retailedge.orders;
create policy "user can update own orders"
on retailedge.orders
for update
to authenticated
using (customer_id = auth.uid())
with check (customer_id = auth.uid());

-- Order items belong to orders owned by the user
drop policy if exists "user can read own order_items" on retailedge.order_items;
create policy "user can read own order_items"
on retailedge.order_items
for select
to authenticated
using (exists (
  select 1 from retailedge.orders o
  where o.id = order_id and o.customer_id = auth.uid()
));

drop policy if exists "user can write own order_items" on retailedge.order_items;
create policy "user can write own order_items"
on retailedge.order_items
for all
to authenticated
using (exists (
  select 1 from retailedge.orders o
  where o.id = order_id and o.customer_id = auth.uid()
))
with check (exists (
  select 1 from retailedge.orders o
  where o.id = order_id and o.customer_id = auth.uid()
));
