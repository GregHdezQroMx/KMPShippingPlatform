import 'package:flutter/material.dart';
import '../../../parser/domain/models/sdui_component.dart';

class SDUICard extends StatelessWidget {
  final SDUICardComponent component;
  final Widget Function(SDUIComponent) childBuilder;

  const SDUICard({
    super.key, 
    required this.component, 
    required this.childBuilder
  });

  @override
  Widget build(BuildContext context) {
    return Card(
      elevation: 2.0,
      margin: const EdgeInsets.symmetric(vertical: 8.0),
      child: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          children: component.children.map((child) => childBuilder(child)).toList(),
        ),
      ),
    );
  }
}
