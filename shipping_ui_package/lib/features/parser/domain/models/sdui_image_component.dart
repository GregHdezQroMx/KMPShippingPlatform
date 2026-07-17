import 'sdui_component.dart';

class SDUIImageComponent extends SDUIComponent {
  final String imageUrl;
  final String? label;

  SDUIImageComponent({
    required super.id,
    required super.type,
    required this.imageUrl,
    this.label,
  });

  factory SDUIImageComponent.fromJson(Map<String, dynamic> json) {
    return SDUIImageComponent(
      id: json['id'] ?? '',
      type: 'image',
      imageUrl: json['imageUrl'] ?? '',
      label: json['label'],
    );
  }
}
