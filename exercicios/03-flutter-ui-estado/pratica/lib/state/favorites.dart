import 'dart:async';

import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:firebase_core/firebase_core.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

const _favoritesDocument = 'favorites/meus-favoritos';

class FavoritesNotifier extends Notifier<Set<int>> {
  int _localRevision = 0;

  @override
  Set<int> build() {
    _listenToFirestore();
    return const <int>{};
  }

  void _listenToFirestore() {
    if (Firebase.apps.isEmpty) return;

    final revisionAtStart = _localRevision;
    var firstSnapshot = true;
    final subscription = FirebaseFirestore.instance
        .doc(_favoritesDocument)
        .snapshots()
        .listen((document) {
      if (firstSnapshot) {
        firstSnapshot = false;
        if (_localRevision != revisionAtStart) return;
      }

      final values = document.data()?['ids'];
      if (values is List) {
        state = values.whereType<num>().map((value) => value.toInt()).toSet();
      } else {
        state = const <int>{};
      }
    }, onError: (Object error) {
      debugPrint('Firestore (ler favoritos): $error');
    });

    ref.onDispose(() => unawaited(subscription.cancel()));
  }

  Future<void> _saveToFirestore(Set<int> favorites) async {
    if (Firebase.apps.isEmpty) return;

    try {
      await FirebaseFirestore.instance.doc(_favoritesDocument).set({
        'ids': favorites.toList()..sort(),
      });
    } catch (error) {
      debugPrint('Firestore (gravar favoritos): $error');
    }
  }

  void toggle(int id) {
    final next = Set<int>.from(state);
    if (!next.add(id)) next.remove(id);

    _localRevision++;
    state = next;
    unawaited(_saveToFirestore(next));
  }

  void clear() {
    _localRevision++;
    state = const <int>{};
    unawaited(_saveToFirestore(state));
  }
}

final favoritesProvider =
    NotifierProvider<FavoritesNotifier, Set<int>>(FavoritesNotifier.new);
