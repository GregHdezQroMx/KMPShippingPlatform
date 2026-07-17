import SharedLogic

class AppDependencies {
    static let shared = AppDependencies()
    
    var calculateQuoteUseCase: CalculateQuoteUseCase {
        // CORRECCIÓN: Eliminamos el .shared.
        // Si Swift te pide el acceso al companion object, se hace así:
        return SharedLogic.KoinPlatform.companion.getShippingUseCase()
    }
}
