import { Product } from "./product";

export interface Lootbox {
    id: number;
    name: string;
    price: number;
    pool: Array<Product>;
    image: string;
  }