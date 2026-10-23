// Shared data: both screens import it, only the id travels in the route
export type Game = {
  id: string; // route params are strings, so ids are strings too
  title: string;
  platform: string;
  year: number;
  description: string;
};

export const games: Game[] = [
  {
    id: "1",
    title: "Celeste",
    platform: "PC",
    year: 2018,
    description: "Plataformas de precisión sobre escalar una montaña.",
  },
  {
    id: "2",
    title: "Hades",
    platform: "Switch",
    year: 2020,
    description: "Roguelike de acción para escapar del inframundo.",
  },
  {
    id: "3",
    title: "Portal 2",
    platform: "PC",
    year: 2011,
    description: "Puzles en primera persona con una pistola de portales.",
  },
];
