import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:filmes_flutter/state/favorites.dart';

void main() {
  test('favorites can be added, toggled off, and cleared', () {
    final container = ProviderContainer();
    addTearDown(container.dispose);

    expect(container.read(favoritesProvider), isEmpty);

    final notifier = container.read(favoritesProvider.notifier);
    notifier.toggle(1);
    expect(container.read(favoritesProvider), {1});

    notifier.toggle(1);
    expect(container.read(favoritesProvider), isEmpty);

    notifier.toggle(1);
    notifier.toggle(2);
    expect(container.read(favoritesProvider), {1, 2});

    notifier.clear();
    expect(container.read(favoritesProvider), isEmpty);
  });
}
