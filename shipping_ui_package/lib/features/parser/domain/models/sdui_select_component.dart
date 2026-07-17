import 'sdui_component.dart';
import 'sdui_option.dart';

class SDUISelectComponent extends SDUIComponent {
  final String label;
  final String? defaultValue;
  final List<SDUIOption> options;

  SDUISelectComponent({
    required super.id,
    required super.type,
    required this.label,
    this.defaultValue,
    required this.options,
  });

  factory SDUISelectComponent.fromJson(Map<String, dynamic> json) {
    return SDUISelectComponent(
      id: json['id'] ?? '',
      type: 'select',
      label: json['label'] ?? '',
      defaultValue: json['defaultValue'],
      options: (json['options'] as List?)
              ?.map((e) => SDUIOption.fromJson(Map<String, dynamic>.from(e)))
              .toList() ??
          [],
    );
  }
}
