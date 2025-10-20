-- Extensions commonly available in Supabase
create extension if not exists "uuid-ossp";
create extension if not exists pgcrypto;

-- Logical schema
create schema if not exists retailedge;

-- Products
create table if not exists retailedge.products (
  id uuid primary key default gen_random_uuid(),
  sku text not null unique,
  name text not null,
  description text,
  price numeric(12,2) not null check (price >= 0),
  stock integer not null default 0 check (stock >= 0),
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now()
);

create index if not exists idx_products_sku on retailedge.products (sku);

-- Order status enum
do $$
begin
  if not exists (select 1 from pg_type where typname = 'order_status') then
    create type order_status as enum ('CREATED','PAID','CANCELLED');
  end if;
end $$;

-- Orders
create table if not exists retailedge.orders (
  id uuid primary key default gen_random_uuid(),
  order_number text not null unique,
  customer_id uuid, -- can reference auth.users(id) if you use Supabase Auth
  status order_status not null default 'CREATED',
  total numeric(12,2) not null default 0 check (total >= 0),
  created_at timestamptz not null default now()
);

-- Order items
create table if not exists retailedge.order_items (
  id uuid primary key default gen_random_uuid(),
  order_id uuid not null references retailedge.orders(id) on delete cascade,
  product_id uuid not null references retailedge.products(id),
  quantity integer not null check (quantity > 0),
  unit_price numeric(12,2) not null check (unit_price >= 0),
  line_total numeric(12,2) generated always as (quantity * unit_price) stored
);

create index if not exists idx_order_items_order on retailedge.order_items (order_id);
create index if not exists idx_order_items_product on retailedge.order_items (product_id);

-- Helper to maintain orders.total
create or replace function retailedge.recalc_order_total(p_order_id uuid)
returns void language plpgsql as $$
begin
  update retailedge.orders o
  set total = coalesce((
    select sum(line_total) from retailedge.order_items oi where oi.order_id = p_order_id
  ), 0)
  where o.id = p_order_id;
end $$;

create or replace function retailedge.trigger_recalc_order_total()
returns trigger language plpgsql as $$
begin
  perform retailedge.recalc_order_total(coalesce(new.order_id, old.order_id));
  return coalesce(new, old);
end $$;

drop trigger if exists trg_recalc_order_total_ins on retailedge.order_items;
drop trigger if exists trg_recalc_order_total_upd on retailedge.order_items;
drop trigger if exists trg_recalc_order_total_del on retailedge.order_items;

create trigger trg_recalc_order_total_ins
after insert on retailedge.order_items
for each row execute function retailedge.trigger_recalc_order_total();

create trigger trg_recalc_order_total_upd
after update on retailedge.order_items
for each row execute function retailedge.trigger_recalc_order_total();

create trigger trg_recalc_order_total_del
after delete on retailedge.order_items
for each row execute function retailedge.trigger_recalc_order_total();
