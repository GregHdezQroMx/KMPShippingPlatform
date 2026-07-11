import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'features/components/presentation/sdui_engine_root.dart';

void main() => runApp(
  const ProviderScope(
    child: MaterialApp(
      debugShowCheckedModeBanner: false,
      home: SDUIEngineRoot(initialJson: ''), // Espera el JSON inicial del nativo
    ),
  ),
);
