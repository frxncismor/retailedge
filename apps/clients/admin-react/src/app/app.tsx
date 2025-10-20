import { Route, Routes } from 'react-router-dom';
import ProductList from '../components/ProductList';
import './app.css';

export function App() {
  return (
    <div className="app">
      <header className="header">
        <div className="container">
          <h1 className="logo">RetailEdge Admin</h1>
          <nav className="nav">
            <a href="/" className="nav-link">
              Products
            </a>
          </nav>
        </div>
      </header>

      <main className="main">
        <Routes>
          <Route path="/" element={<ProductList />} />
        </Routes>
      </main>
    </div>
  );
}

export default App;
