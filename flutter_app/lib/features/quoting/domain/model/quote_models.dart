enum ShippingType { standard, express }

class QuoteRequest {
  final double weightKg;
  final double distanceKm;
  final ShippingType shippingType;
  final String destinationZipCode;

  QuoteRequest({
    required this.weightKg,
    required this.distanceKm,
    required this.shippingType,
    required this.destinationZipCode,
  });

  Map<String, dynamic> toJson() => {
        'weightKg': weightKg,
        'distanceKm': distanceKm,
        'shippingType': shippingType.name.toUpperCase(),
        'destinationZipCode': destinationZipCode,
      };
}

class QuoteResponse {
  final double finalPrice;
  final String currency;
  final int estimatedDays;
  final QuoteDetail details;

  QuoteResponse({
    required this.finalPrice,
    required this.currency,
    required this.estimatedDays,
    required this.details,
  });

  factory QuoteResponse.fromJson(Map<String, dynamic> json) {
    return QuoteResponse(
      finalPrice: (json['finalPrice'] as num).toDouble(),
      currency: json['currency'] ?? 'MXN',
      estimatedDays: json['estimatedDays'] as int,
      details: QuoteDetail.fromJson(json['details']),
    );
  }

  Map<String, dynamic> toJson() => {
        'finalPrice': finalPrice,
        'currency': currency,
        'estimatedDays': estimatedDays,
        'details': details.toJson(),
      };
}

class QuoteDetail {
  final double baseTariff;
  final ShippingType shippingType;
  final bool specialHandlingApplied;
  final bool foreignZoneApplied;
  final double remoteMultiplier;

  QuoteDetail({
    required this.baseTariff,
    required this.shippingType,
    required this.specialHandlingApplied,
    required this.foreignZoneApplied,
    required this.remoteMultiplier,
  });

  factory QuoteDetail.fromJson(Map<String, dynamic> json) {
    return QuoteDetail(
      baseTariff: (json['baseTariff'] as num).toDouble(),
      shippingType: ShippingType.values.firstWhere(
        (e) => e.name.toUpperCase() == json['shippingType'],
        orElse: () => ShippingType.standard,
      ),
      specialHandlingApplied: json['specialHandlingApplied'] as bool,
      foreignZoneApplied: json['foreignZoneApplied'] as bool,
      remoteMultiplier: (json['remoteMultiplier'] as num).toDouble(),
    );
  }

  Map<String, dynamic> toJson() => {
        'baseTariff': baseTariff,
        'shippingType': shippingType.name.toUpperCase(),
        'specialHandlingApplied': specialHandlingApplied,
        'foreignZoneApplied': foreignZoneApplied,
        'remoteMultiplier': remoteMultiplier,
      };
}

abstract class QuoteResult {}

class QuoteSuccess extends QuoteResult {
  final QuoteResponse data;
  QuoteSuccess(this.data);
}

class QuoteError extends QuoteResult {
  final QuoteErrorType type;
  final String code;
  final String message;

  QuoteError({
    required this.type,
    required this.code,
    required this.message,
  });

  Map<String, dynamic> toJson() => {
        'status': 'ERROR',
        'error': {
          'type': type.name.toUpperCase(),
          'code': code,
          'message': message,
        }
      };
}

enum QuoteErrorType { validationError, remoteServiceError }
