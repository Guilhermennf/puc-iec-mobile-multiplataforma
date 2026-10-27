// src/__tests__/tmdb.service.test.ts
// ✅ AVALIATIVO — valida o TODO 1 (fetchPopularMovies)

import { describe, it, expect, vi, beforeEach } from 'vitest';
import { fetchPopularMovies, tmdbClient } from '../../services/tmdb';

const fakeMovies = [
  { id: 1, title: 'Matrix', overview: 'Neo.', poster_path: '/m.jpg', release_date: '1999-03-31', vote_average: 8.7 },
  { id: 2, title: 'Inception', overview: 'Dom.', poster_path: '/i.jpg', release_date: '2010-07-16', vote_average: 8.8 },
];

beforeEach(() => {
  vi.restoreAllMocks();
});

describe('fetchPopularMovies', () => {
  it('1. retorna lista de filmes via tmdbClient', async () => {
    vi.spyOn(tmdbClient, 'get').mockResolvedValue({
      data: { results: fakeMovies, page: 1, total_pages: 5, total_results: 100 },
    });

    const response = await fetchPopularMovies();

    expect(tmdbClient.get).toHaveBeenCalledWith(
      '/movie/popular',
      expect.objectContaining({
        params: expect.objectContaining({ language: 'pt-BR', page: 1 }),
      }),
    );
    expect(response.results).toHaveLength(2);
    expect(response.results[0].title).toBe('Matrix');
  });

  it('2. propaga erro quando tmdbClient lança (ex.: token inválido)', async () => {
    vi.spyOn(tmdbClient, 'get').mockRejectedValue(new Error('Request failed with status code 401'));

    await expect(fetchPopularMovies()).rejects.toThrow('401');
  });

  it('3. inclui a página solicitada na chamada', async () => {
    vi.spyOn(tmdbClient, 'get').mockResolvedValue({
      data: { results: fakeMovies, page: 3, total_pages: 5, total_results: 100 },
    });

    await fetchPopularMovies(3);

    expect(tmdbClient.get).toHaveBeenCalledWith(
      '/movie/popular',
      expect.objectContaining({
        params: expect.objectContaining({ language: 'pt-BR', page: 3 }),
      }),
    );
  });
});
